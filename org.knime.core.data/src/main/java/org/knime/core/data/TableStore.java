package org.knime.core.data;

import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.Domain;

public interface TableStore {

	<T, V extends DataChunkAccess<T>> ColumnStore<T, V> getStore(long index);

	Domain getDomain(long index);

	long getNumColumns();
}
