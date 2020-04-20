package org.knime.core.data.data.types;

import org.knime.core.data.api.column.access.DoubleReadAccess;
import org.knime.core.data.api.column.access.DoubleWriteAccess;
import org.knime.core.data.data.DataAccess;

public interface DoubleData extends NumericData {

	// TODO if the method call here is costly, we can get rid of this call by
	// letting the backend implement their own ArrayAccess
	public static class DoubleAccess implements DataAccess<DoubleData>, DoubleReadAccess, DoubleWriteAccess {

		private DoubleData m_array;
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
		public void updateStorage(DoubleData array) {
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
