
package org.knime.core.data.arrow;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

import org.apache.arrow.memory.RootAllocator;
import org.knime.core.data.store.DataStoreFactory;
import org.knime.core.data.store.types.BooleanStore;
import org.knime.core.data.store.types.DoubleStore;
import org.knime.core.data.store.types.StringStore;

public class ArrowStoreFactory implements DataStoreFactory {

	private final String STORE_ID = UUID.randomUUID().toString();

	private final long m_chunkSize;
	private final RootAllocator m_allocator;
	private final Path m_baseDir;

	public ArrowStoreFactory(final long chunkSize, final long offheapSize) throws IOException {
		m_chunkSize = RootAllocator.nextPowerOfTwo(chunkSize);
		m_allocator = new RootAllocator(offheapSize);

		// TODO we also want to delete this guy on exit...
		m_baseDir = Files.createTempDirectory("ArrowStore_" + STORE_ID);
	}

	@Override
	public DoubleStore<?> createDoubleStore() {
		return new Float8VectorStore(m_allocator.newChildAllocator("TODO DoubleStore", 0, m_allocator.getLimit()),
				createFile("DoubleStore"), m_chunkSize);
	}

	@Override
	public BooleanStore<?> createBooleanStore() {
		return new BitVectorStore(m_allocator.newChildAllocator("TODO BitVectorStore", 0, m_allocator.getLimit()),
				createFile("BooleanStore"), m_chunkSize);
	}

	@Override
	public StringStore<?> createStringStore() {
		return new VarCharVectorStore(m_allocator.newChildAllocator("TODO StringStore", 0, m_allocator.getLimit()),
				createFile("StringStore"), m_chunkSize);
	}

	private File createFile(String prefix) {
		try {
			final File file = Files.createTempFile(mbaseDir, prefix + "_" + UUID.randomUUID().toString(), ".knarrow")
					.toFile();
			file.deleteOnExit();
			return file;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void close() throws Exception {
		m_allocator.close();
	}
}
