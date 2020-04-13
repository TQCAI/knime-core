package org.knime.core.data.arrow;

import java.io.File;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.api.column.domain.NumericDomain;
import org.knime.core.data.arrow.Float8VectorStore.Float8VectorAccess;
import org.knime.core.data.store.Data;
import org.knime.core.data.store.types.DoubleStore;

public class Float8VectorStore extends AbstractArrowStore<Float8Vector, Float8VectorAccess>
		implements DoubleStore<Float8Vector, Float8VectorAccess> {

	private Float8VectorDomain m_domain;

	Float8VectorStore(BufferAllocator allocator, File file, long chunkSize) {
		super(allocator, file, chunkSize);
		m_domain = new Float8VectorDomain();
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

	@Override
	public NumericDomain getDomain() {
		return m_domain;
	}

	@Override
	protected void updateDomain(Data<Float8Vector> data) {
		m_domain.update(data.get());
	}

	final class Float8VectorDomain implements NumericDomain {

		private long m_numMissing = 0;
		private long m_numNonMissing = 0;
		private double m_min = Double.MAX_VALUE;
		private double m_max = Double.MIN_VALUE;
		private double m_sum = 0;

		void update(final Float8Vector vector) {
			for (int i = 0; i < vector.getValueCount(); i++) {
				if (vector.isNull(i)) {
					m_numMissing++;
				} else {
					final double val = vector.get(i);
					m_min = Math.min(val, m_min);
					m_max = Math.max(val, m_max);
					m_sum += val;
				}
			}

			m_numNonMissing = vector.getValueCount() - m_numMissing;
		}

		@Override
		public long getNumNonMissing() {
			return m_numNonMissing;
		}

		@Override
		public long getNumMissing() {
			return m_numMissing;
		}

		@Override
		public double getMin() {
			return m_min;
		}

		@Override
		public double getMax() {
			return m_max;
		}

		@Override
		public double getSum() {
			return m_sum;
		}

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
