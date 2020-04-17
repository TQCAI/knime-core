package org.knime.core.data.store;

public interface ReferencedData<T> {

	/**
	 * Release reference on object
	 */
	void release();

	/**
	 * Retain refernce on data object
	 */
	void retain();
	
}
