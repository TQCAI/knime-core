
package org.knime.core.data.column;

import org.knime.core.data.access.WriteAccess;

public interface ColumnWriteTable {
	long getNumColumns();

	WritableColumn<? extends WriteAccess> getWriteColumn(long columnIndex);
}
