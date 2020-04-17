package org.knime.core.data.store.types;

import org.knime.core.data.api.column.access.ReadableBooleanAccess;
import org.knime.core.data.api.column.access.WritableBooleanAccess;
import org.knime.core.data.store.DataAccess;

public interface BooleanAccess<T> extends DataAccess<T>, ReadableBooleanAccess, WritableBooleanAccess {
	// NB: Marker interface
}