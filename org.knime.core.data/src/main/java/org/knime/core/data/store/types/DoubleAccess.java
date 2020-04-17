package org.knime.core.data.store.types;

import org.knime.core.data.api.column.access.DoubleReadAccess;
import org.knime.core.data.api.column.access.DoubleWriteAccess;
import org.knime.core.data.store.DataAccess;

public interface DoubleAccess<T> extends DataAccess<T>, DoubleWriteAccess, DoubleReadAccess {
	// NB: Marker interface
}