
package org.knime.core.data.api.row;

import org.knime.core.data.api.column.ReadAccess;

public interface ReadableRowCursor extends AutoCloseable {

	boolean canFwd();

	void fwd();

	ReadAccess getReadableAccess(long index);
}
