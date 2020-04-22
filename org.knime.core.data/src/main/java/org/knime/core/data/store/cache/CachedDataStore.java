package org.knime.core.data.store.cache;

import java.io.Flushable;
import java.io.IOException;
import java.util.function.Supplier;

import org.knime.core.data.Data;
import org.knime.core.data.DataReader;
import org.knime.core.data.DataWriter;
import org.knime.core.data.store.DataStore;

/*
 * TODO thread-safety
 * TODO implement 'pre-flushing' and 'pre-loading'
 * TODO cache interface
 */
public final class CachedDataStore<D extends Data> implements Flushable, DataStore<D> {

	private final CachedDataReadStore<D> m_readCache;
	private final DataCache<D> m_cache;

	private final DataWriter<D> m_writer;
	private final DataWriter<D> m_cachedWriter;

	public CachedDataStore(final DataWriter<D> writer, Supplier<DataReader<D>> readers) {
		m_writer = writer;
		m_cachedWriter = new DataWriter<D>() {

			@Override
			public void write(D data) {
				m_cache.put(data);
			}

			@Override
			public void close() throws Exception {
				m_writer.close();
			}
		};
		m_cache = new DataCache<>();
		m_readCache = new CachedDataReadStore<>(readers);
	}

	@Override
	public void flush() throws IOException {
		// TODO best strategies?
		while (!m_cache.isFullyFlushed())
			m_cache.flushNext(true, m_writer);
	}

	@Override
	public DataReader<D> createReader() {
		return m_readCache.createReader();
	}

	@Override
	public DataWriter<D> getWriter() {
		return m_cachedWriter;
	}
}
