package org.knime.core.data;

import java.io.Flushable;
import java.io.IOException;

public class CachedTableStore implements TableStore, Flushable {

	private final CachedColumnStore<?, ?>[] m_columnStores;

	public CachedTableStore(TableStore delegate) {
		// TODO long..
		m_columnStores = new CachedColumnStore[(int) delegate.getNumColumns()];
		for (long i = 0; i < m_columnStores.length; i++) {
			m_columnStores[(int) i] = new CachedColumnStore<>(delegate.getStore(i));
		}
	}

	@Override
	public long getNumColumns() {
		return m_columnStores.length;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends DataAccess<T>> ColumnDataStore<T, V> getStore(long index) {
		// NB: this cast is OK. just dealing with array not being able to capture
		// generics.
		return (ColumnDataStore<T, V>) m_columnStores[(int) index];
	}

	@Override
	public void flush() throws IOException {
		for (int i = 0; i < m_columnStores.length; i++) {
			// TODO this guy could act as a central cache manager for all columns. For
			// example flush non-frequently used columns etc or force pre-loading or maybe
			// nothing.
			m_columnStores[i].flush();
		}
	}
}
