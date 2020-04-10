package org.knime.core.data;

import org.knime.core.data.column.Domain;

public interface ReadableRowTable {
	long getNumColumns();

	Domain getDomain(long index);

	ReadableRowCursor getRowCursor();
}
