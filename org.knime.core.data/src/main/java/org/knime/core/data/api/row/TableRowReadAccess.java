package org.knime.core.data.api.row;

public interface TableRowReadAccess {
	long getNumColumns();

	RowReadCursor createRowCursor();
}
