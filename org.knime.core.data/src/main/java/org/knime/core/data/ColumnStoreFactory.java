package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public interface ColumnStoreFactory {

	<T, D extends DataChunk<T>, V extends DataChunkAccess<T>> ColumnStore<T, D, V> createColumnStore(
			NativeColumnType type);

}
