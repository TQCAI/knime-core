package org.knime.core.data.store;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.NativeColumnType;

class DefaultTableStore implements TableStore {

	private List<DataStore<?, ?>> m_dataStores;
	private DataStoreFactory m_factory;

	public DefaultTableStore(final DataStoreFactory factory, final ColumnType... types) {
		m_factory = factory;
		m_dataStores = new ArrayList<DataStore<?, ?>>();
		for (int i = 0; i < types.length; i++) {
			final NativeColumnType[] nativeTypes = types[i].getNativeTypes();
			if (nativeTypes.length == 1) {
				m_dataStores.add(create(factory, nativeTypes[0]));
			} else {
				// TODO only single level struct or allow infinite nesting?
				// TODO push struct creation down to dataformat? Possible e.g. via opening and
				// closing a datafactory for a nested type.
				final List<DataStore<?, ?>> structList = new ArrayList<>();
				for (NativeColumnType type : nativeTypes) {
					structList.add(create(factory, type));
				}
				m_dataStores.add(new StructDataStore(structList));
			}

		}
	}

	private DataStore<?, ?> create(final DataStoreFactory factory, final NativeColumnType type) {
		switch (type) {
		case BOOLEAN:
			return factory.createBooleanStore();
		case DOUBLE:
			return factory.createDoubleStore();
		case STRING:
			return factory.createStringStore();
		default:
			throw new UnsupportedOperationException("Unknown type " + type);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends StoreDataAccess<T>> DataStore<T, V> getStore(long index) {
		// NB: OK Cast.
		return (DataStore<T, V>) m_dataStores.get((int) index);
	}

	@Override
	public long getNumColumns() {
		return m_dataStores.size();
	}

	@Override
	public void close() throws Exception {
		// TODO close here or in factory?
		for (final DataStore<?, ?> store : m_dataStores) {
			store.close();
		}
		m_factory.close();
	}

}
