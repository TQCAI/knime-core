package org.knime.core.data.chunked;

import org.knime.core.data.Data;
import org.knime.core.data.column.Cursor;

public interface ChunkedDataCursor<T, D extends Data<T>> extends Cursor<D> {
	// TODO marker interface
}
