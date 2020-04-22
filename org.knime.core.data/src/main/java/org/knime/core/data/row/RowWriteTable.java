package org.knime.core.data.row;

public interface RowWriteTable {

	int getNumColumns();

	RowWriteCursor cursor();
}
