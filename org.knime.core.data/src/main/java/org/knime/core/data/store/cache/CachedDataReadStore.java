package org.knime.core.data.store.cache;

import java.util.function.Supplier;

import org.knime.core.data.Data;
import org.knime.core.data.DataReader;
import org.knime.core.data.store.DataReadStore;
import org.knime.core.data.store.DataReaderConfig;

// TODO thread-safety
// TODO implement 'pre-loading'
public final class CachedDataReadStore<D extends Data, C extends DataReaderConfig> implements DataReadStore<D, C> {

	private final Supplier<DataReader<D>> m_readers;
	private final DataCache<D> m_cache;

	public CachedDataReadStore(Supplier<DataReader<D>> readers) {
		this(readers, new DataCache<>());
	}

	CachedDataReadStore(Supplier<DataReader<D>> readers, DataCache<D> cache) {
		m_readers = readers;
		m_cache = cache;
	}

	@Override
	public DataReader<D> createReader(C config) {
		return new DataReader<D>() {

			final DataReader<D> m_reader = m_readers.get();

			@Override
			public void close() throws Exception {
				m_reader.close();
			}

			@Override
			public D read(long index) {
				return m_cache.getOrLoad(index, m_reader);
			}

			@Override
			public long size() {
				return m_reader.size();
			}
		};
	}
}
