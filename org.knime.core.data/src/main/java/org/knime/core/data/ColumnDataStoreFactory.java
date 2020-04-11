package org.knime.core.data;

public interface ColumnDataStoreFactory {

	<T, V extends DataAccess<T>> ColumnDataStore<T, V> createDoubleColumnStore();

}
