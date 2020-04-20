
package org.knime.core.data.api;

import org.knime.core.data.api.column.ReadColumn;

public interface ReadTable {

	long getNumColumns();

	ReadColumn<?> getReadColumn(long columnIndex);
}
