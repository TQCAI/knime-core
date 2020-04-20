package org.knime.core.data.data.types;

import org.knime.core.data.data.Data;

public interface NumericData extends Data {

	// NB: Marker interface for Numeric data
	double getDouble(int index);

	void setDouble(int index, double val);
}
