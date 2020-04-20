package org.knime.core.data.data.cache;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.data.ConsumingDataStore;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.LoadingDataStore;
import org.knime.core.data.data.table.ColumnBatchWriter;
import org.knime.core.data.data.table.TableDataReadAccess;
import org.knime.core.data.data.table.TableDataWriteAccess;

/*
 * TODO thread-safety
 * TODO implement 'pre-flushing' and 'pre-loading'
 */
public final class CachedDataStore implements Flushable, ConsumingDataStore, LoadingDataStore {

	private final List<DataCache<?>> m_caches;
	// TODO move into cache?
	private final List<DataConsumer<?>> m_writers;

	private final NativeType<?, ?>[] m_types;

	private final CachedLoadingDataStore m_readCache;
	private final ColumnBatchWriter<Data> m_writer;

	public CachedDataStore(NativeType<?, ?>[] types, TableDataWriteAccess writer, TableDataReadAccess reader) {
		m_types = types;
		m_readCache = new CachedLoadingDataStore(types, reader);
		m_writers = new ArrayList<>(types.length);
		m_caches = new ArrayList<>(types.length);

		m_writer = writer.getWriter();
		for (int i = 0; i < types.length; i++) {
			m_caches.add(new DataCache<>());
			m_writers.add((data) -> m_writer.consume(data));
		}
	}

	@Override
	public <D extends Data> DataConsumer<D> getConsumer(long columnIndex) {
		return new DataConsumer<D>() {
			@Override
			public void accept(D data) {
				@SuppressWarnings("unchecked")
				final DataCache<D> cache = (DataCache<D>) m_caches.get((int) columnIndex);
				cache.put(index, data);
			}

		};
	}

	// TODO interface
	// TODO make IO strategy configurable (column first, record batches, etc).
	// Currently implemented: RecordBatches
	public void flush() throws IOException {
		/*
		 * TODO we can implement multiple strategies here: flush by column, flush by
		 * record batch, flush by ... we can also use additional information for the
		 * order of flush (e.g. which column is actually still being used -> ref
		 * counting).
		 */
		final Data[] collected = new Data[m_caches.size()];
		for (int i = 0; i < collected.length; i++) {
			final DataCache<? extends Data> cache = m_caches.get(i);

			/*
			 * TODO If one cache is fully flushed, all others should be as as well? how to
			 * handle these scenarios with missing data in columns? not possible with
			 * Row-API but certainly possible with column API. Append Missing values?
			 */
			if (!cache.isFullyFlushed()) {
				break;
			}

			cache.flushNext(true, cast(m_writers.get(i)));
		}
		m_writer.flush();
		clear();
	}

	@Override
	public <D extends Data> DataLoader<D> createLoader(long columnIndex) {
		return m_readCache.createLoader(columnIndex);
	}

	@Override
	public void close() throws Exception {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).close();
		}
		m_writer.close();
	}

	@Override
	public NativeType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	private void clear() {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).clear();
		}
	}

	private <D extends Data> DataConsumer<D> cast(DataConsumer<?> dataConsumer) {
		@SuppressWarnings("unchecked")
		final DataConsumer<D> cast = (DataConsumer<D>) dataConsumer;
		return cast;
	}

}
