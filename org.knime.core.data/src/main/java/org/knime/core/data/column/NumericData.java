package org.knime.core.data.column;

// NB: Marker interface for Numeric data
public interface NumericData extends ColumnData {

	double getDouble(int index);

	void setDouble(int index, double val);
}

