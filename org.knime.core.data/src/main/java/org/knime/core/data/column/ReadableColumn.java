
package org.knime.core.data.column;

import org.knime.core.data.Cursor;
import org.knime.core.data.access.ReadAccess;

public interface ReadableColumn<A extends ReadAccess> {

	/**
	 * @return a new cursor over the column. Must be closed when done.
	 */
	Cursor<? extends A> cursor();

}
