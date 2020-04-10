package org.knime.core.data;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.Domain;

class DefaultTableStore implements TableStore {

	private List<ColumnStore<?, ?, ?>> m_columnStores;

	public DefaultTableStore(final ColumnStoreFactory factory, final ColumnType... types) {
		m_columnStores = new ArrayList<ColumnStore<?, ?, ?>>();
		for (int i = 0; i < types.length; i++) {
			// TODO deal with multiple native types
			m_columnStores.add(factory.createColumnStore(types[i].getNativeTypes()[0]));
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, C extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, C, V> getStore(long index) {
		// NB: OK Cast.
		return (ColumnStore<T, C, V>) m_columnStores.get((int) index);
	}

	@Override
	public Domain getDomain(long index) {
		return m_columnStores.get((int) index).getDomain();
	}

	@Override
	public long getNumColumns() {
		return m_columnStores.size();
	}

}
