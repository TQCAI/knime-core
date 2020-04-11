package org.knime.core.data;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.column.ColumnType;

class DefaultTableStore implements TableStore {

	private List<ColumnDataStore<?, ?>> m_columnStores;

	public DefaultTableStore(final ColumnDataStoreFactory factory, final ColumnType... types) {
		m_columnStores = new ArrayList<ColumnDataStore<?, ?>>();
		for (int i = 0; i < types.length; i++) {
			// TODO deal with multiple native types
			m_columnStores.add(factory.createColumnDataStore(types[i].getNativeTypes()[0]));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends DataAccess<T>> ColumnDataStore<T, V> getStore(long index) {
		// NB: OK Cast.
		return (ColumnDataStore<T, V>) m_columnStores.get((int) index);
	}

	@Override
	public long getNumColumns() {
		return m_columnStores.size();
	}

}
