
package org.knime.core.data.table;

import org.knime.core.data.column.ReadableColumn;

public interface ReadableTable {

	long getNumColumns();

	ReadableColumn<?, ?> getReadableColumn(long columnIndex);
}
