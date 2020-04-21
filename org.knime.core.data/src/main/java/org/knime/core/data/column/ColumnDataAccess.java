package org.knime.core.data.column;

import org.knime.core.data.DataAccess;
import org.knime.core.data.access.ReadAccess;
import org.knime.core.data.access.WriteAccess;

public interface ColumnDataAccess<D extends ColumnData>
		extends DataAccess<D>, WriteAccess, ReadAccess {
	// NB: Marker interface for column data.
}
