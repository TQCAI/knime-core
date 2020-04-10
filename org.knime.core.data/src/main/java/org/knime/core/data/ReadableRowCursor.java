
package org.knime.core.data;

import org.knime.core.data.column.ReadableAccess;

//TODO isn't that actually a cursor over 'WritableColumnAccess[]'?
public interface ReadableRowCursor extends AutoCloseable {

	boolean canFwd();

	void fwd();

	ReadableAccess getReadableAccess(long index);
}
