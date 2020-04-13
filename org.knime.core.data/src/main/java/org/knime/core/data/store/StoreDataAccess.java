package org.knime.core.data.store;

import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.WritableAccess;

public interface StoreDataAccess<T> extends ReadableAccess, WritableAccess {

	/**
	 * Sets the underlying data. Resets internal index to -1.
	 * 
	 * @param data the underlying data
	 */
	void setData(final T data);

	/**
	 * Increments internal index on data
	 */
	void incIndex();

}
