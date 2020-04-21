package org.knime.core.data;

import org.knime.core.data.access.DoubleReadAccess;
import org.knime.core.data.access.DoubleWriteAccess;
import org.knime.core.data.column.ColumnDataAccess;

public interface DoubleData extends NumericData {

	// TODO if the method call here is costly, we can get rid of this call by
	// letting the backend implement their own ArrayAccess
	public static class DoubleAccess implements ColumnDataAccess<DoubleData>, DoubleReadAccess, DoubleWriteAccess {

		private DoubleData m_data;
		private int m_index = -1;

		@Override
		public void setDouble(double value) {
			m_data.setDouble(m_index, value);
		}

		@Override
		public double getDouble() {
			return m_data.getDouble(m_index);
		}

		@Override
		public boolean isMissing() {
			return m_data.isMissing(m_index);
		}

		@Override
		public void setMissing() {
			m_data.setMissing(m_index);
		}

		@Override
		public void update(DoubleData array) {
			m_data = array;
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
