
package org.knime.core.data.api;

import org.knime.core.data.api.column.WritableColumn;

public interface WritableTable {

	long getNumColumns();

	WritableColumn<?> getWritableColumn(long columnIndex);
}
