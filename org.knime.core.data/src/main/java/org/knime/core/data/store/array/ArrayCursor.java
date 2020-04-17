package org.knime.core.data.store.array;

import org.knime.core.data.api.column.Cursor;

public interface ArrayCursor<T extends ArrayAccess<?>> extends Cursor<T> {
	// NB: Marker interface
}
