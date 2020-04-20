package org.knime.core.data.api;

public interface Referenced {

	/**
	 * Release reference
	 */
	void release();

	/**
	 * Retain reference
	 */
	void retain();

}
