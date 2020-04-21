
package org.knime.core.data.api.column;

public interface TableColumnWriteAccess {
	long getNumColumns();

	WriteColumn<? extends WriteAccess> getWriteColumn(long columnIndex);
}
