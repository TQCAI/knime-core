
package org.knime.core.data.column;

public interface ColumnReadableTable {

	long getNumColumns();

	ReadableColumn<?> getReadColumn(long columnIndex);
}
