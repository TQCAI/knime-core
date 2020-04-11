
package org.knime.core.data.array;

import org.knime.core.data.DataStore;
import org.knime.core.data.DataStoreFactory;
import org.knime.core.data.DataAccess;
import org.knime.core.data.chunk.ReadableData;
import org.knime.core.data.column.NativeColumnType;

public class ArrayColumnStoreFactory implements DataStoreFactory {

	private final long m_chunkSize;

	public ArrayColumnStoreFactory(final long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@Override
	public <T, V extends DataAccess<T>> DataStore<T, V> createColumnDataStore(NativeColumnType type) {
		// TODO Auto-generated method stub
		return null;
	}
}
