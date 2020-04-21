package org.knime.core.data.store.cache;

import java.io.Flushable;
import java.io.IOException;

import org.knime.core.data.Data;
import org.knime.core.data.DataReader;
import org.knime.core.data.DataWriter;
import org.knime.core.data.store.DataReaderConfig;
import org.knime.core.data.store.DataStore;

/*
 * TODO thread-safety
 * TODO implement 'pre-flushing' and 'pre-loading'
 * TODO cache interface
 */
public final class CachedDataStore<D extends Data, C extends DataReaderConfig> implements Flushable, DataStore<D, C> {

	private final CachedDataReadStore<D, C> m_readCache;
	private final DataCache<D> m_cache;

	private final DataWriter<D> m_writer;
	private final DataWriter<D> m_cachedWriter;
	private final DataStore<D, C> m_store;

	public CachedDataStore(DataStore<D, C> store, C config) {
		m_store = store;
		m_writer = store.getWriter();
		m_cachedWriter = new DataWriter<D>() {

			@Override
			public void write(D data) {
				m_cache.put(data);
			}

			@Override
			public void close() {
				m_writer.close();
			}
		};
		m_cache = new DataCache<>();
		m_readCache = new CachedDataReadStore<>(() -> store.createReader(config));
	}

	@Override
	public void flush() throws IOException {
		// TODO best strategies?
		while (!m_cache.isFullyFlushed())
			m_cache.flushNext(true, m_writer);
	}

	@Override
	public DataReader<D> createReader(C config) {
		return m_readCache.createReader(config);
	}

	@Override
	public DataWriter<D> getWriter() {
		return m_cachedWriter;
	}

	@Override
	public void close() throws Exception {
		m_store.close();
	}
}
