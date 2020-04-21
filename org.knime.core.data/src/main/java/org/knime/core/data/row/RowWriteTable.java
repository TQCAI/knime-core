package org.knime.core.data.row;

import org.knime.core.data.Cursor;

public interface RowWriteTable {

	long getNumColumns();

	Cursor<? extends RowWriteAccess> getRowCursor();
}
