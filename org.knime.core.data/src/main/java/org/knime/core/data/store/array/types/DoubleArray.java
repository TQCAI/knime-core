package org.knime.core.data.store.array.types;

import org.knime.core.data.api.column.access.DoubleReadAccess;
import org.knime.core.data.api.column.access.DoubleWriteAccess;
import org.knime.core.data.store.array.Array;
import org.knime.core.data.store.array.ArrayAccess;

public interface DoubleArray extends Array {
	double getDouble(long index);

	void setDouble(long index, double val);

	// TODO if the method call here is costly, we can get rid of this call by
	// letting the backend implement their own ArrayAccess
	public static class DoubleArrayAccess implements ArrayAccess<DoubleArray>, DoubleReadAccess, DoubleWriteAccess {

		private DoubleArray m_array;
		private long m_index = -1;

		@Override
		public void setDouble(double val) {
			m_array.setDouble(m_index, val);
		}

		@Override
		public double getDouble() {
			return m_array.getDouble(m_index);
		}

		@Override
		public boolean isMissing() {
			return m_array.isMissing(m_index);
		}

		@Override
		public void setMissing() {
			m_array.setMissing(m_index);
		}

		@Override
		public void updateStorage(DoubleArray array) {
			m_array = array;
			m_index = 0;
		}

		@Override
		public void fwd() {
			m_index++;
		}

		@Override
		public void reset() {
			m_index = -1;
		}

	}

}
