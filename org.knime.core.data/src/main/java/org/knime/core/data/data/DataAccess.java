package org.knime.core.data.data;

import org.knime.core.data.api.column.ReadAccess;
import org.knime.core.data.api.column.WritableAccess;

public interface DataAccess<A extends Data> extends ReadAccess, WritableAccess {

	/**
	 * Updates underlying storage. Updates access.
	 * 
	 * @param array
	 */
	void updateStorage(A array);

	void fwd();

	void reset();
}
