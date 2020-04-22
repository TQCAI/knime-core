package org.knime.core.data.row;

public interface RowReadTable {
	long getNumColumns();

	RowReadCursor newCursor();
}
