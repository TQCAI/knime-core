
package org.knime.core.data;

import org.knime.core.data.column.WritableAccess;

// TODO isn't that actually a cursor over 'WritableColumnAccess[]'?
public interface WritableRow extends AutoCloseable {

	void fwd();

	WritableAccess getWritableAccess(long index);
}
