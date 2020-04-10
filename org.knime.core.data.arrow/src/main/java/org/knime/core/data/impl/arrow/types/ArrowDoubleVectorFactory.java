
package org.knime.core.data.impl.arrow.types;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.access.ReadableDoubleAccess;
import org.knime.core.data.access.WritableDoubleAccess;

public class ArrowDoubleVectorFactory extends AbstractArrowFieldVectorFactory<Float8Vector> {

	public ArrowDoubleVectorFactory(final BufferAllocator allocator, final int partitionCapacity) {
		super(allocator, partitionCapacity);
	}

	@Override
	Float8Vector create(final BufferAllocator allocator, final int capacity) {
		final Float8Vector vector = new Float8Vector((String) null, allocator);
		vector.allocateNew(capacity);
		return vector;
	}

	public static final class DoubleArrowValue//
			extends AbstractArrowValue<Float8Vector> //
			implements ReadableDoubleAccess, WritableDoubleAccess {

		@Override
		public double getDoubleValue() {
			return m_vector.get(m_index);
		}

		@Override
		public void setDoubleValue(final double value) {
			m_vector.set(m_index, value);
		}
	}
}
