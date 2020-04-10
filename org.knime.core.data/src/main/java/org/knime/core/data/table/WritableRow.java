
package org.knime.core.data.table;

import org.knime.core.data.column.WritableAccess;

// TODO isn't that actually a cursor over 'WritableColumnAccess[]'?
public interface WritableRow extends AutoCloseable {

	void fwd();

	long getNumColumns();

	WritableAccess getWritableAccess(long index);
}
