
package org.knime.core.data.api.row;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.WriteAccess;

public interface RowWriteCursor extends AutoCloseable, Cursor<WriteAccess[]> {
// NB: Marker interface
}
