package org.knime.core.data.column.logical;

import org.knime.core.data.column.ColumnDataAccess;

public interface LogicalDataAccess<L extends LogicalData> extends ColumnDataAccess<L> {
	long getNumColumns();

	ColumnDataAccess<?> getColumnAccess(int index);
}
