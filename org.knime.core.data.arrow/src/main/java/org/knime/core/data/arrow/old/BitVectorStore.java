package org.knime.core.data.arrow.old;

import java.io.File;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.BitVector;
import org.knime.core.data.api.access.ReadableBooleanAccess;
import org.knime.core.data.api.access.WritableBooleanAccess;
import org.knime.core.data.arrow.old.BitVectorStore.BitVectorAccess;
import org.knime.core.data.store.Data;
import org.knime.core.data.store.Data;
import org.knime.core.data.store.types.BooleanStore;

public class BitVectorStore extends AbstractArrowStore<BitVector, BitVectorAccess>
		implements BooleanStore<BitVector, BitVectorAccess> {

	BitVectorStore(BufferAllocator allocator, File file, long chunkSize) {
		super(allocator, file, chunkSize);
	}

	@Override
	public BitVectorAccess createDataAccess() {
		return new BitVectorAccess();
	}

	@Override
	protected BitVector create(BufferAllocator allocator, long capacity) {
		final BitVector vector = new BitVector((String) null, allocator);
		vector.allocateNew((int) capacity);
		return vector;
	}

	static final class BitVectorAccess //
			extends AbstractFieldVectorAccess<BitVector> //
			implements ReadableBooleanAccess, WritableBooleanAccess {

		@Override
		public boolean getBooleanValue() {
			return m_vector.get(m_index) > 0;
		}

		@Override
		public void setBooleanValue(final boolean value) {
			m_vector.set(m_index, value ? 1 : 0);
		}
	}

	@Override
	public DataDomain<BitVector> getDomain() {
		throw new UnsupportedOperationException("not yet supported");
	}

}
