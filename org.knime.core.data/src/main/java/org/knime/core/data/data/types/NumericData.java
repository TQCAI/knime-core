package org.knime.core.data.data.types;

import org.knime.core.data.data.Data;

public interface NumericData extends Data {

	// NB: Marker interface for Numeric data
	double getDouble(long index);

	void setDouble(long index, double val);
}
