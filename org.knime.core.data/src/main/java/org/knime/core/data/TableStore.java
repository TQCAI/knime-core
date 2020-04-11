package org.knime.core.data;

public interface TableStore {

	<T, V extends DataAccess<T>> ColumnDataStore<T, V> getStore(long index);

	long getNumColumns();
}
