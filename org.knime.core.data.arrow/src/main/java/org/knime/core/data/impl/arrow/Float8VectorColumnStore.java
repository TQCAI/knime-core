package org.knime.core.data.impl.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.access.ReadableDoubleAccess;
import org.knime.core.data.access.WritableDoubleAccess;
import org.knime.core.data.impl.arrow.Float8VectorColumnStore.Float8VectorAccess;

public class Float8VectorColumnStore extends AbstractColumnStore<Float8Vector, Float8VectorAccess> {

	Float8VectorColumnStore(BufferAllocator allocator, long chunkSize) {
		super(allocator, chunkSize);
	}

	@Override
	public Float8VectorAccess createDataAccess() {
		return new Float8VectorAccess();
	}

	@Override
	protected Float8Vector create(BufferAllocator allocator, long capacity) {
		final Float8Vector vector = new Float8Vector((String) null, allocator);
		vector.allocateNew((int) capacity);
		return vector;
	}

	final static class Float8VectorAccess extends AbstractFieldVectorAccess<Float8Vector> //
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
