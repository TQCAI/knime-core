
package org.knime.core.data.impl.arrow.types;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.BitVector;
import org.knime.core.data.access.ReadableBooleanAccess;
import org.knime.core.data.access.WritableBooleanAccess;

public final class ArrowBitVectorFactory extends AbstractArrowFieldVectorFactory<BitVector> {

	public ArrowBitVectorFactory(final BufferAllocator allocator, final int partitionCapacity) {
		super(allocator, partitionCapacity);
	}

	@Override
	BitVector create(final BufferAllocator allocator, final int capacity) {
		final BitVector vector = new BitVector((String) null, allocator);
		vector.allocateNew(capacity);
		return vector;
	}

	public static final class BooleanArrowValue //
			extends AbstractArrowValue<BitVector> //
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
