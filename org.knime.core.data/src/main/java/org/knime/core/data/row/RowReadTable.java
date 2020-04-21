package org.knime.core.data.row;

import org.knime.core.data.Cursor;

public interface RowReadTable {
	long getNumColumns();

	Cursor<? extends RowReadAccess> createRowReadCursor();
}
