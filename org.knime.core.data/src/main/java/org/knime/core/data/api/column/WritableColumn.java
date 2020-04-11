package org.knime.core.data.api.column;

public interface WritableColumn<V extends WritableAccess> {
	/**
	 * @return a new cursor over the column. Must be closed when done.
	 */
	WritableCursor<V> createWritableCursor();

}
