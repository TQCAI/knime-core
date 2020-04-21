package org.knime.core.data.row;

import org.knime.core.data.access.WriteAccess;

public interface RowWriteAccess {
	WriteAccess getWriteAccess(int index);
}
