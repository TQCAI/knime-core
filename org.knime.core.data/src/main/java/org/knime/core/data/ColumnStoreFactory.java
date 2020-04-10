package org.knime.core.data;

import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.NativeColumnType;

public interface ColumnStoreFactory {

	<T, V extends DataChunkAccess<T>> ColumnStore<T, V> createColumnStore(NativeColumnType type);

}
