package org.knime.core.data.row;

import org.knime.core.data.access.ReadAccess;

public interface RowReadAccess {
	ReadAccess getReadAccess(int index);
}
