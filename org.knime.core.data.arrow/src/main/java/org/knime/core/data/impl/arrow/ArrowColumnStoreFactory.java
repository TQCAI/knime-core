
package org.knime.core.data.impl.arrow;

import java.io.IOException;
import java.util.UUID;

import org.apache.arrow.memory.RootAllocator;
import org.knime.core.data.ColumnDataStore;
import org.knime.core.data.ColumnDataStoreFactory;
import org.knime.core.data.DataAccess;
import org.knime.core.data.chunk.ReadableData;
import org.knime.core.data.column.NativeColumnType;

public class ArrowColumnStoreFactory implements ColumnDataStoreFactory {

	private final String STORE_ID = UUID.randomUUID().toString();

	private final long m_chunkSize;
	private final RootAllocator m_allocator;
//	private final Path m_baseDir;

	public ArrowColumnStoreFactory(final long chunkSize, final long offheapSize) throws IOException {
		m_chunkSize = RootAllocator.nextPowerOfTwo(chunkSize);
		m_allocator = new RootAllocator(offheapSize);
//		m_baseDir = Files.createTempDirectory("ArrowStore_" + STORE_ID);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T, D extends ReadableData<T>, V extends DataAccess<T>> ColumnDataStore<T, D, V> createColumnDataStore(
		final NativeColumnType type)
	{
		switch (type) {
			case BOOLEAN:
				return (ColumnDataStore<T, D, V>) new BitVectorColumnStore(m_allocator.newChildAllocator(type.toString(), 0,
					m_allocator.getLimit()), m_chunkSize);
			case DOUBLE:
				return (ColumnDataStore<T, D, V>) new Float8VectorColumnStore(m_allocator.newChildAllocator(type.toString(), 0,
					m_allocator.getLimit()), m_chunkSize);
			case STRING:
				return (ColumnDataStore<T, D, V>) new VarCharVectorColumnStore(m_allocator.newChildAllocator(type.toString(), 0,
					m_allocator.getLimit()), m_chunkSize);
			default:
				throw new IllegalStateException("Type '" + type + "' not supported.");
		}
	}
}
