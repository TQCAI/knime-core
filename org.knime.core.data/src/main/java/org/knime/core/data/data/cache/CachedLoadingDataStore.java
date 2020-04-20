package org.knime.core.data.data.cache;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.PrimitiveType;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.LoadingDataStore;
import org.knime.core.data.data.table.TableDataReadAccess;

// TODO thread-safety
// TODO implement 'pre-flushing' and 'pre-loading'
public final class CachedLoadingDataStore implements LoadingDataStore {

	private final List<DataCache<?>> m_caches;
	private final TableDataReadAccess m_data;
	private final PrimitiveType[] m_types;

	public CachedLoadingDataStore(PrimitiveType[] types, TableDataReadAccess data) {
		this(types, data, new ArrayList<>((int) types.length));

		for (int i = 0; i < types.length; i++) {
			m_caches.add(new DataCache<>(null));
		}
	}

	/**
	 * @param types
	 * @param data
	 * @param caches
	 */
	CachedLoadingDataStore(PrimitiveType[] types, TableDataReadAccess data, List<DataCache<?>> caches) {
		m_data = data;
		m_types = types;
		m_caches = caches;
	}

	@Override
	public <D extends Data> DataLoader<D> createLoader(long columnIndex) {
		return new DataLoader<D>() {

			// create new delegate per loader
			private final DataLoader<D> m_loader;

			{
				m_loader = m_data.createLoader(columnIndex);
			}

			@Override
			public D load(long index) {
				@SuppressWarnings("unchecked")
				final DataCache<D> cache = (DataCache<D>) m_caches.get((int) index);
				return cache.getOrLoad(index, m_loader);
			}

			@Override
			public long size() {
				return m_loader.size();
			}

			@Override
			public void close() throws Exception {
				m_loader.close();
			}
		};
	}

	@Override
	public void close() throws Exception {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).close();
		}
		clear();
	}

	@Override
	public PrimitiveType[] getPrimitiveSpec() {
		return m_types;
	}

	// release all data from caches. keeps caches open until close
	// TODO Interface?
	public void clear() {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).clear();
		}
	}

}
