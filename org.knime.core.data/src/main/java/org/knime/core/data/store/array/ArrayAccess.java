package org.knime.core.data.store.array;

import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.WritableAccess;

public interface ArrayAccess<A extends Array> extends ReadableAccess, WritableAccess {

	/**
	 * Updates underlying storage. Updates access.
	 * 
	 * @param array
	 */
	void updateStorage(A array);

	void fwd();

	void reset();
}
