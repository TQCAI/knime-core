package org.knime.core.data;

import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.column.WritableAccess;

public interface DataAccess<T> extends ReadableAccess, WritableAccess {

	void update(final T data);

	void incIndex();

}
