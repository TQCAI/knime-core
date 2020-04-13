package org.knime.core.data.store;

public interface TableStore extends AutoCloseable {

	<T, V extends StoreDataAccess<T>> DataStore<T, V> getStore(long index);

	long getNumColumns();
}
