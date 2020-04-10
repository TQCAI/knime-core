package org.knime.core.data.chunk;

import org.knime.core.data.column.Cursor;

public interface DataChunkCursor<T, D extends DataChunk<T>> extends Cursor<D> {
	void move(long steps);
}
