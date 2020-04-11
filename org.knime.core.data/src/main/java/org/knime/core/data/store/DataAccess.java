package org.knime.core.data.store;

import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.WritableAccess;

public interface DataAccess<T> extends ReadableAccess, WritableAccess {

	void update(final T data);

	void incIndex();

}
