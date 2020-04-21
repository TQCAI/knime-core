package org.knime.core.data.api.row;

public interface TableRowWriteAccess {

	long getNumColumns();

	RowWriteCursor getRowCursor();
}
