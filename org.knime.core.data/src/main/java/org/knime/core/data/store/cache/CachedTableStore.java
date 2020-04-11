package org.knime.core.data.store.cache;

import java.io.Flushable;
import java.io.IOException;

import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataStore;
import org.knime.core.data.store.TableStore;

public class CachedTableStore implements TableStore, Flushable {

	private final CachedDataStore<?, ?>[] m_stores;
	private TableStore m_delegate;

	public CachedTableStore(TableStore delegate) {
		// TODO long..
		m_delegate = delegate;
		m_stores = new CachedDataStore[(int) delegate.getNumColumns()];
		for (long i = 0; i < m_stores.length; i++) {
			m_stores[(int) i] = new CachedDataStore<>(delegate.getStore(i));
		}
	}

	@Override
	public long getNumColumns() {
		return m_stores.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends DataAccess<T>> DataStore<T, V> getStore(long index) {
		// NB: this cast is OK. just dealing with array not being able to capture
		// generics.
		return (DataStore<T, V>) m_stores[(int) index];
	}

	@Override
	public void flush() throws IOException {
		for (int i = 0; i < m_stores.length; i++) {
			// TODO this guy could act as a central cache manager for all columns. For
			// example flush non-frequently used columns etc or force pre-loading or maybe
			// nothing.
			m_stores[i].flush();
		}
	}

	@Override
	public void close() throws Exception {
		// close means clear memory
		for (final CachedDataStore<?, ?> store : m_stores) {
			store.close();
		}

		// Close delegate!
		m_delegate.close();
	}
}
