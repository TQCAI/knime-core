package org.knime.core.data.array;

import org.knime.core.data.ColumnStore;
import org.knime.core.data.ColumnStoreFactory;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public class ArrayColumnStoreFactory implements ColumnStoreFactory {

	private long m_chunkSize;

	public ArrayColumnStoreFactory(long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, V extends DataChunkAccess<T>> ColumnStore<T, V> createColumnStore(NativeColumnType type) {
		// TODO we could even make this extensible
		switch (type) {
		case BOOLEAN:
			break;
		case DOUBLE:
			return (ColumnStore<T, V>) new DoubleColumnStore(m_chunkSize);
		case STRING:
			break;
		default:
			break;

		}
		return null;
	}

}
