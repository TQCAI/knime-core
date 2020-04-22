package org.knime.core.data.row;

import org.knime.core.data.Cursor;

public interface RowWriteTable extends AutoCloseable {

	long getNumColumns();
	
	Cursor<? extends RowWriteAccess> getRowCursor();
}
