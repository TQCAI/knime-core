package org.knime.core.data.api.row;

import org.knime.core.data.api.column.domain.Domain;

public interface ReadableRowTable {
	long getNumColumns();

	Domain getDomain(long index);

	ReadableRowCursor getRowCursor();
}
