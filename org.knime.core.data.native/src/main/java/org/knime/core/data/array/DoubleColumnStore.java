package org.knime.core.data.array;

import org.knime.core.data.access.ReadableDoubleAccess;
import org.knime.core.data.access.WritableDoubleAccess;
import org.knime.core.data.array.DoubleColumnStore.DoubleArray;
import org.knime.core.data.array.DoubleColumnStore.DoubleArrayAccess;
import org.knime.core.data.column.Domain;

public class DoubleColumnStore extends AbstractArrayColumnStore<DoubleArray, DoubleArrayAccess> {

	DoubleColumnStore(long chunkSize) {
		super(chunkSize);
	}

	@Override
	public DoubleArrayAccess createDataAccess() {
		return new DoubleArrayAccess();
	}

	@Override
	protected DoubleArray create(long capacity) {
		return new DoubleArray(capacity);
	}

	@Override
	protected Domain initDomain() {
		// TODO return empty NumericColumnDomain
		return null;
	}

	static class DoubleArray extends AbstractNativeArray<double[]> {
		DoubleArray(long capacity) {
			super(new double[(int) capacity], (int) capacity);
		}
	}

	static final class DoubleArrayAccess//
			extends AbstractNativeArrayAccess<double[], DoubleArray> //
			implements ReadableDoubleAccess, WritableDoubleAccess {

		@Override
		public double getDoubleValue() {
			return m_data[m_index];
		}

		@Override
		public void setDoubleValue(final double value) {
			m_data[m_index] = value;
		}
	}
}
