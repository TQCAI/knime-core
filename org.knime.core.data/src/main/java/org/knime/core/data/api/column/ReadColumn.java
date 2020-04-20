
package org.knime.core.data.api.column;

public interface ReadColumn<A extends ReadAccess> {

	/**
	 * @return a new cursor over the column. Must be closed when done.
	 */
	Cursor<? extends A> cursor();

}
