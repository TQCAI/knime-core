package org.knime.core.data.column.logical;

import org.knime.core.data.access.ReadAccess;
import org.knime.core.data.access.WriteAccess;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnDataAccess;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.logical.LogicalType.DateTimeType.DateTimeAccess;
import org.knime.core.data.column.logical.LogicalType.DateTimeType.DateTimeData;

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
			public ReadAccess read() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public WriteAccess write() {
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
			// TODO Auto-generated method stub
			return null;
		}
	}
}
