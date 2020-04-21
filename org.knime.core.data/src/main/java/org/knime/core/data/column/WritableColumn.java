package org.knime.core.data.column;

import org.knime.core.data.Cursor;
import org.knime.core.data.access.WriteAccess;

public interface WritableColumn<A extends WriteAccess> {

	/**
	 * TODO Current contract: only one writable cursor per column.
	 * 
	 * @return a new cursor over the column. Must be closed when done.
	 */
	Cursor<? extends A> access();

}
