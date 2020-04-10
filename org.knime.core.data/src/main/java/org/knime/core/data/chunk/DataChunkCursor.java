package org.knime.core.data.chunk;

import org.knime.core.data.column.Cursor;

public interface DataChunkCursor<D extends DataChunk<?>> extends Cursor<D> {
	void move(long steps);
}
