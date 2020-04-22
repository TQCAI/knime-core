package org.knime.core.data;

// TODO type on ReadAcces / WriteAccess?
public interface DataAccess<D> {

	/**
	 * Updates underlying data.
	 * 
	 * @param data
	 */
	void update(D data);

	/**
	 * Fwd the internal cursor
	 */
	void fwd();

	/**
	 * resets internal cursor to -1
	 */
	void reset();
}
