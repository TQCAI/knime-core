package org.knime.core.data;

import org.knime.core.data.column.ColumnData;

public interface NumericData extends ColumnData {

	// NB: Marker interface for Numeric data
	double getDouble(int index);

	void setDouble(int index, double val);
}
