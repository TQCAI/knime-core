
package org.knime.core.data.table;

import org.knime.core.data.column.ReadableAccess;

//TODO isn't that actually a cursor over 'WritableColumnAccess[]'?
public interface ReadableRow extends AutoCloseable {

	boolean canFwd();

	void fwd();

	long getNumColumns();

	ReadableAccess getReadableAccess(long index);
}
