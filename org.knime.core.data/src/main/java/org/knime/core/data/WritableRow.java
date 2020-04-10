
package org.knime.core.data;

import org.knime.core.data.column.WritableAccess;

public interface WritableRow extends AutoCloseable {

	void fwd();

	WritableAccess getWritableAccess(long index);
}
