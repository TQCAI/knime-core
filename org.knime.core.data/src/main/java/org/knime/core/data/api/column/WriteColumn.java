package org.knime.core.data.api.column;

public interface WriteColumn<A extends WritableAccess> {

	/**
	 * TODO Current contract: only one writable cursor per column.
	 * 
	 * @return a new cursor over the column. Must be closed when done.
	 */
	Cursor<? extends A> access();

}
