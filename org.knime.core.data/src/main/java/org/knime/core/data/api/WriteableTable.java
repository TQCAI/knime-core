
package org.knime.core.data.api;

import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;

public interface WriteableTable {

	long getNumColumns();

	WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex);
}
