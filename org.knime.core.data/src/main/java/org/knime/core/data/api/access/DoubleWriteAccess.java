package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WriteAccess;

public interface DoubleWriteAccess extends WriteAccess {

	void setDouble(double value);
}