package org.knime.core.data.chunk;

import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.column.WritableAccess;

public interface DataChunkAccess<T> extends ReadableAccess, WritableAccess {

	void update(final T data);

	void incIndex();

}
