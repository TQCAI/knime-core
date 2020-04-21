
package org.knime.core.data.api.column;

public interface TableColumnReadAccess {

	long getNumColumns();

	ReadColumn<?> getReadColumn(long columnIndex);
}
