package org.knime.core.data.chunk;

import org.knime.core.data.column.Cursor;

public interface DataChunkCursor<T> extends Cursor<DataChunk<T>> {
	void move(long steps);
}
