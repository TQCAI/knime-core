package org.knime.core.data.array;

import org.knime.core.data.ColumnStore;
import org.knime.core.data.ColumnStoreFactory;
import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public class ArrayColumnStoreFactory implements ColumnStoreFactory {

	private long m_chunkSize;

	public ArrayColumnStoreFactory(long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, D extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, D, V> createColumnStore(
			NativeColumnType type) {
		// TODO we could even make this extensible
		switch (type) {
		case BOOLEAN:
			break;
		case DOUBLE:
			return (ColumnStore<T, D, V>) new DoubleColumnStore(m_chunkSize);
		case STRING:
			break;
		default:
			break;

		}
		return null;
	}

}
