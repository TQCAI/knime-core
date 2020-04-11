package org.knime.core.data.store;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.NativeColumnType;

class DefaultTableStore implements TableStore {

	private List<DataStore<?, ?>> m_dataStores;

	public DefaultTableStore(final DataStoreFactory factory, final ColumnType... types) {
		m_dataStores = new ArrayList<DataStore<?, ?>>();
		for (int i = 0; i < types.length; i++) {
			final NativeColumnType[] nativeTypes = types[i].getNativeTypes();
			if (nativeTypes.length == 1) {
				switch (nativeTypes[0]) {
				case BOOLEAN:
					m_dataStores.add(factory.createBooleanStore());
					break;
				case DOUBLE:
					m_dataStores.add(factory.createDoubleStore());
					break;
				case STRING:
					m_dataStores.add(factory.createStringStore());
					break;
				default:
					throw new UnsupportedOperationException("Unknown type " + nativeTypes[0]);
				}
			} else {
				// TODO
				throw new UnsupportedOperationException("Multi types are not yet implemented");
			}

		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends DataAccess<T>> DataStore<T, V> getStore(long index) {
		// NB: OK Cast.
		return (DataStore<T, V>) m_dataStores.get((int) index);
	}

	@Override
	public long getNumColumns() {
		return m_dataStores.size();
	}

	@Override
	public void close() throws Exception {
		for (final DataStore<?, ?> store : m_dataStores) {
			store.close();
		}
	}

}
