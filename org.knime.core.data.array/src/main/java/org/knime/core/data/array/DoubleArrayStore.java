package org.knime.core.data.array;

import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.array.DoubleArrayStore.DoubleArray;
import org.knime.core.data.array.DoubleArrayStore.DoubleArrayAccess;
import org.knime.core.data.store.DataDomain;
import org.knime.core.data.store.types.DoubleStore;

public class DoubleArrayStore extends AbstractArrayStore<DoubleArray, DoubleArrayAccess>
		implements DoubleStore<DoubleArray, DoubleArrayAccess> {

	DoubleArrayStore(long chunkSize) {
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

	@Override
	public DataDomain<DoubleArray> getDomain() {
		throw new UnsupportedOperationException("not yet supported");
	}
}
