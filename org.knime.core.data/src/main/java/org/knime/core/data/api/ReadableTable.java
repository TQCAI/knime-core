
package org.knime.core.data.api;

import org.knime.core.data.api.column.ReadableColumn;

public interface ReadableTable {

	long getNumColumns();

	ReadableColumn<?, ?> getReadableColumn(long columnIndex);
}
