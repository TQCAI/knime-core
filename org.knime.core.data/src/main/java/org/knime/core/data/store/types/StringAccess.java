package org.knime.core.data.store.types;

import org.knime.core.data.api.column.access.ReadableStringAccess;
import org.knime.core.data.api.column.access.WritableStringAccess;
import org.knime.core.data.store.DataAccess;

public interface StringAccess<T> extends DataAccess<T>, ReadableStringAccess, WritableStringAccess {
	// NB: Marker interface
}
