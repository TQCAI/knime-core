package org.knime.core.data.column;

import org.knime.core.data.column.ColumnType.DoubleType.DoubleAccess;
import org.knime.core.data.column.ColumnType.DoubleType.DoubleData;
import org.knime.core.data.value.DoubleReadValue;
import org.knime.core.data.value.DoubleWriteValue;
import org.knime.core.data.value.WriteValue;

// looks like an enum, but isn't! We want to support nesting, complex types etc.
// TODO Allow to create empty domains for types. Interface. Not every type has a domain currently.
public interface ColumnType<D extends ColumnData, A extends ColumnDataAccess<D>> {

	A createAccess();

	// NB: Marker interface for Numeric data
	public static interface NumericData extends ColumnData {

		double getDouble(int index);

		void setDouble(int index, double val);
	}

	final static class DoubleType implements ColumnType<DoubleData, DoubleAccess> {

		public static DoubleType INSTANCE = new DoubleType();

		private DoubleType() {
		}

		@Override
		public DoubleAccess createAccess() {
			return new DoubleAccess();
		}

		public static interface DoubleData extends NumericData {
			// NB: data. get/set double. inherited from NumericData
		}

		public static class DoubleAccess implements ColumnDataAccess<DoubleData> {

			private DoubleData m_data;
			private int m_index = -1;

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

			@Override
			public DoubleReadValue read() {
				// TODO slow?
				return new DoubleReadValue() {
					@Override
					public boolean isMissing() {
						return m_data.isMissing(m_index);
					}

					@Override
					public double getDouble() {
						return m_data.getDouble(m_index);
					}
				};
			}

			@Override
			public WriteValue write() {
				// TODO slow?
				return new DoubleWriteValue() {

					@Override
					public void setMissing() {
						m_data.setMissing(m_index);
					}

					@Override
					public void setDouble(double value) {
						m_data.setDouble(m_index, value);
					}
				};
			}
		}
	}
}
