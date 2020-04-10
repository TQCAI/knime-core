
package org.knime.core.data.column;

public interface ReadableColumn<V extends ReadableAccess, D extends Domain> {

	/**
	 * @return a new cursor over the column. Must be closed when done.
	 */
	ReadableCursor<V> createReadableCursor();

	D getDomain();

}
