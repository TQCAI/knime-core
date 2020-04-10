
package org.knime.core.data.array;

import org.knime.core.data.ColumnStore;
import org.knime.core.data.ColumnStoreFactory;
import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public class ArrayColumnStoreFactory implements ColumnStoreFactory {

	private final long m_chunkSize;

	public ArrayColumnStoreFactory(final long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, D extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, D, V> createColumnStore(
		final NativeColumnType type)
	{
		// TODO we could even make this extensible
		switch (type) {
			case BOOLEAN:
				return (ColumnStore<T, D, V>) new BooleanColumnStore(m_chunkSize);
			case DOUBLE:
				return (ColumnStore<T, D, V>) new DoubleColumnStore(m_chunkSize);
			case STRING:
				return (ColumnStore<T, D, V>) new StringColumnStore(m_chunkSize);
			default:
				throw new IllegalStateException("Type '" + type + "' not supported.");
		}
	}
}
