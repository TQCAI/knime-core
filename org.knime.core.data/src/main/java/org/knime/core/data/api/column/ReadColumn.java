
package org.knime.core.data.api.column;

import org.knime.core.data.api.column.domain.Domain;

public interface ReadColumn<A extends ReadableAccess> {

	/**
	 * @return a new cursor over the column. Must be closed when done.
	 */
	Cursor<? extends A> cursor();

	/**
	 * @return domain of this column
	 */
	Domain getDomain();

}
