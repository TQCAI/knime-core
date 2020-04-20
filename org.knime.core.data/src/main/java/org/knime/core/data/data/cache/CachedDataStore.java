package org.knime.core.data.data.cache;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.PrimitiveType;
import org.knime.core.data.data.ConsumingDataStore;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.DataWriter;
import org.knime.core.data.data.LoadingDataStore;
import org.knime.core.data.data.table.TableDataReadAccess;
import org.knime.core.data.data.table.TableDataWriteAccess;

// TODO thread-safety
// TODO implement 'pre-flushing' and 'pre-loading'
public final class CachedDataStore implements Flushable, ConsumingDataStore, LoadingDataStore {

	private final List<DataCache<?>> m_caches;
	private final PrimitiveType[] m_types;

	private final TableDataWriteAccess m_delegate;
	private CachedLoadingDataStore m_readCache;

	public CachedDataStore(PrimitiveType[] types, TableDataWriteAccess writer, TableDataReadAccess reader) {
		m_delegate = writer;
		m_readCache = new CachedLoadingDataStore(types, reader);
		m_types = types;
		m_caches = new ArrayList<>((int) types.length);

		for (int i = 0; i < types.length; i++) {
			m_caches.add(new DataCache<>(m_delegate.getWriter(i)));
		}
	}

	@Override
	public <D extends Data> DataConsumer<D> getConsumer(long columnIndex) {
		return new DataConsumer<D>() {

			private final DataWriter<D> m_writer;

			{
				m_writer = m_delegate.getWriter(columnIndex);
			}

			@Override
			public void accept(long index, D data) {
				@SuppressWarnings("unchecked")
				DataCache<D> cache = (DataCache<D>) m_caches.get((int) index);
				cache.put(index, data);
			}

			@Override
			public void close() throws Exception {
				// close writer (we only consider single, non-parallel write for now)
				m_writer.close();
			}
		};
	}

	public void flush() throws IOException {
		// TODO we can implement multiple strategies here:
		// flush by column, flush by record batch, flush by ...
		// we can also use additional information for the order of flush (e.g. which
		// column is actually still being used -> ref counting).
		for (final DataCache<?> cache : m_caches) {
			if (!cache.isFlushed())
				cache.flushNext(true);
		}

		clear();
	}

	@Override
	public void close() throws Exception {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).close();
		}
	}

	@Override
	public PrimitiveType[] getPrimitiveSpec() {
		return m_types;
	}

	// release all data from caches. keeps caches open until close
	private void clear() {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).clear();
		}
	}

	@Override
	public <D extends Data> DataLoader<D> createLoader(long columnIndex) {
		return m_readCache.createLoader(columnIndex);
	}

}
