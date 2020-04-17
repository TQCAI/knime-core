package org.knime.core.data.store;

public interface DataAccess<S> {

	/**
	 * Sets the underlying data. Calls 'reset()'.
	 * 
	 * @param data the underlying data storage
	 */
	void set(final S data);

	/**
	 * Increments internal index on data
	 */
	void fwd();

	/**
	 * resets internal cursor to -1
	 */
	void reset();

}
