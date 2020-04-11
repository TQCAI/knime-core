
package org.knime.core.data.api.row;

import org.knime.core.data.api.column.WritableAccess;

public interface WritableRow extends AutoCloseable {

	void fwd();

	WritableAccess getWritableAccess(long index);
}
