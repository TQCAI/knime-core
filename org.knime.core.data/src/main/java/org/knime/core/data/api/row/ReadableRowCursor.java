
package org.knime.core.data.api.row;

import org.knime.core.data.api.column.ReadableAccess;

public interface ReadableRowCursor extends AutoCloseable {

	boolean canFwd();

	void fwd();

	ReadableAccess getReadableAccess(long index);
}
