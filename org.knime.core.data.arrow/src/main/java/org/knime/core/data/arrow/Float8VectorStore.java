package org.knime.core.data.arrow;

import java.io.File;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.arrow.Float8VectorStore.Float8VectorAccess;
import org.knime.core.data.store.types.DoubleStore;

public class Float8VectorStore extends AbstractArrowStore<Float8Vector, Float8VectorAccess>
		implements DoubleStore<Float8Vector, Float8VectorAccess> {

	Float8VectorStore(BufferAllocator allocator, File file, long chunkSize) {
		super(allocator, file, chunkSize);
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

	final class Float8VectorAccess extends AbstractFieldVectorAccess<Float8Vector> //
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
