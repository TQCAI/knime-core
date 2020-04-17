package org.knime.core.data.api.column.access;

import org.knime.core.data.api.column.WritableAccess;

public interface DoubleWriteAccess extends WritableAccess {

	void setDouble(double value);
}