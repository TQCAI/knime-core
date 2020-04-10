package org.knime.core.data.impl.arrow;

import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

import org.apache.arrow.memory.RootAllocator;
import org.knime.core.data.ColumnStore;
import org.knime.core.data.ColumnStoreFactory;
import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public class ArrowColumnStoreFactory implements ColumnStoreFactory {
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
	public <T, D extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, D, V> createColumnStore(
			NativeColumnType type) {
		switch (type) {
		case BOOLEAN:
			return (ColumnStore<T, D, V>) new BitVectorColumnStore(
					m_allocator.newChildAllocator(type.toString(), 0, m_allocator.getLimit()), m_chunkSize);
		case DOUBLE:
			return (ColumnStore<T, D, V>) new Float8VectorColumnStore(
					m_allocator.newChildAllocator(type.toString(), 0, m_allocator.getLimit()), m_chunkSize);
		case STRING:
			return (ColumnStore<T, D, V>) new VarCharVectorColumnStore(
					m_allocator.newChildAllocator(type.toString(), 0, m_allocator.getLimit()), m_chunkSize);
		default:
			break;
		}
		// TODO Auto-generated method stub
		return null;
	}

}
