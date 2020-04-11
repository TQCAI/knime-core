
package org.knime.core.data.array;

import org.knime.core.data.ColumnDataStore;
import org.knime.core.data.ColumnDataStoreFactory;
import org.knime.core.data.DataAccess;
import org.knime.core.data.chunk.ReadableData;
import org.knime.core.data.column.NativeColumnType;

public class ArrayColumnStoreFactory implements ColumnDataStoreFactory {

	private final long m_chunkSize;

	public ArrayColumnStoreFactory(final long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@Override
	public <T, V extends DataAccess<T>> ColumnDataStore<T, V> createColumnDataStore(NativeColumnType type) {
		// TODO Auto-generated method stub
		return null;
	}
}
