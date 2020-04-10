package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.Domain;

public interface TableStore {

	<T, C extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, C, V> getStore(long index);

	Domain getDomain(long index);

	long getNumColumns();
}
