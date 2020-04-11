package org.knime.core.data.impl.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.BitVector;
import org.knime.core.data.access.ReadableBooleanAccess;
import org.knime.core.data.access.WritableBooleanAccess;
import org.knime.core.data.impl.arrow.BitVectorColumnStore.BitVectorAccess;

public class BitVectorColumnStore extends AbstractColumnStore<BitVector, BitVectorAccess> {

	BitVectorColumnStore(BufferAllocator allocator, long chunkSize) {
		super(allocator, chunkSize);
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
}
