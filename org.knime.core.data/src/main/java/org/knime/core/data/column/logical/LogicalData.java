package org.knime.core.data.column.logical;

import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnType;

public interface LogicalData extends ColumnData {

	ColumnData getColumnData(long index);

	ColumnType<?, ?> getColumnType(long index);

	public long getNumColumns();

}
