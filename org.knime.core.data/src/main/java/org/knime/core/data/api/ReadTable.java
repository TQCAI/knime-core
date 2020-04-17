
package org.knime.core.data.api;

import org.knime.core.data.api.column.ReadColumn;

public interface ReadTable {

	long getNumColumns();

	ReadColumn<?> getReadableColumn(long columnIndex);
}
