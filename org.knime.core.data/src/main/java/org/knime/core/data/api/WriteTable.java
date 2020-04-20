
package org.knime.core.data.api;

import org.knime.core.data.api.column.WriteAccess;
import org.knime.core.data.api.column.WriteColumn;

public interface WriteTable {

	long getNumColumns();

	WriteColumn<? extends WriteAccess> getWriteColumn(long columnIndex);
}
