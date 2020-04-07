package org.knime.core.data.array.types;

import org.knime.core.data.table.value.ReadableDoubleValue;
import org.knime.core.data.table.value.WritableDoubleValue;

public class DoubleArray extends AbstractArray<double[]> {

	DoubleArray(int capacity) {
		super(new double[capacity], capacity);
	}

	public static final class NativeDoubleValue//
			extends AbstractNativeValue<double[], DoubleArray> //
			implements ReadableDoubleValue, WritableDoubleValue {

		@Override
		public double getDoubleValue() {
			return m_array[m_index];
		}

		@Override
		public void setDoubleValue(final double value) {
			m_array[m_index] = value;
		}
	}

}
