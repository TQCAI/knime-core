package org.knime.core.data.column.logical;

import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnDataAccess;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.logical.LogicalType.DateTimeType.DateTimeAccess;
import org.knime.core.data.column.logical.LogicalType.DateTimeType.DateTimeData;
import org.knime.core.data.value.ReadValue;
import org.knime.core.data.value.WriteValue;

/**
 * A logical type representing a composition of multiple physical types, e.g.
 * DateTime
 */
public interface LogicalType<L extends LogicalData, A extends LogicalDataAccess<L>> extends ColumnType<L, A> {
	// NB: Marker interface for logical types

	class DateTimeType implements LogicalType<DateTimeData, DateTimeAccess> {

		class DateTimeAccess implements LogicalDataAccess<DateTimeData> {

			@Override
			public void update(DateTimeData data) {
				// TODO Auto-generated method stub

			}

			@Override
			public void fwd() {
				// TODO Auto-generated method stub

			}

			@Override
			public void reset() {
				// TODO Auto-generated method stub

			}

			@Override
			public long getNumColumns() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public ColumnDataAccess<?> getColumnAccess(int index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ReadValue read() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WriteValue write() {
				// TODO Auto-generated method stub
				return null;
			}

		}

		class DateTimeData implements LogicalData {

			@Override
			public void setMissing(int index) {
				// TODO Auto-generated method stub

			}

			@Override
			public boolean isMissing(int index) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public int getMaxCapacity() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getNumValues() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public void setNumValues(int numValues) {
				// TODO Auto-generated method stub

			}

			@Override
			public void release() {
				// TODO Auto-generated method stub

			}

			@Override
			public void retain() {
				// TODO Auto-generated method stub

			}

			@Override
			public ColumnData getColumnData(long index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public ColumnType<?, ?> getColumnType(long index) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public long getNumColumns() {
				// TODO Auto-generated method stub
				return 0;
			}

		}

		@Override
		public DateTimeAccess createAccess() {
			return new DateTimeAccess();
		}

		public static class DateTimeReadValue implements ReadValue {
			@Override
			public boolean isMissing() {
				// TODO Auto-generated method stub
				return false;
			}

		}

		public static class DateTimeWriteValue implements WriteValue {
			@Override
			public void setMissing() {
				// TODO Auto-generated method stub

			}
		}
	}
}
