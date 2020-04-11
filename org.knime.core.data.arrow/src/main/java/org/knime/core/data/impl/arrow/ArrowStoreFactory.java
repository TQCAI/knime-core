
package org.knime.core.data.impl.arrow;

import java.io.IOException;

import org.apache.arrow.memory.RootAllocator;
import org.knime.core.data.store.DataStoreFactory;
import org.knime.core.data.store.types.BooleanDataStore;
import org.knime.core.data.store.types.DoubleDataStore;
import org.knime.core.data.store.types.StringDataStore;

public class ArrowStoreFactory implements DataStoreFactory {

//	private final String STORE_ID = UUID.randomUUID().toString();

	private final long m_chunkSize;
	private final RootAllocator m_allocator;
//	private final Path m_baseDir;

	public ArrowStoreFactory(final long chunkSize, final long offheapSize) throws IOException {
		m_chunkSize = RootAllocator.nextPowerOfTwo(chunkSize);
		m_allocator = new RootAllocator(offheapSize);
//		m_baseDir = Files.createTempDirectory("ArrowStore_" + STORE_ID);
	}

	@Override
	public DoubleDataStore<?, ?> createDoubleStore() {
		return new Float8VectorStore(m_allocator.newChildAllocator("TODO", 0, m_allocator.getLimit()),
				m_chunkSize);
	}

	@Override
	public BooleanDataStore<?, ?> createBooleanStore() {
		return new BitVectorStore(m_allocator.newChildAllocator("TODO", 0, m_allocator.getLimit()), m_chunkSize);
	}

	@Override
	public StringDataStore<?, ?> createStringStore() {
		return new VarCharVectorStore(m_allocator.newChildAllocator("TODO", 0, m_allocator.getLimit()),
				m_chunkSize);
	}
}
