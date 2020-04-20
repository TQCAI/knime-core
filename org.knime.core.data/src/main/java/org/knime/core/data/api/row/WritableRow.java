
package org.knime.core.data.api.row;

import org.knime.core.data.api.column.WriteAccess;

public interface WritableRow extends AutoCloseable {

	void fwd();

	WriteAccess getWritableAccess(long index);
}
