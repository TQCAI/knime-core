package org.knime.core.data.column;

import org.knime.core.data.DataAccess;

public interface ColumnDataAccess<D extends ColumnData> extends DataAccess<D> {
	// NB: Marker interface for DataAccess with ColumnData
}
