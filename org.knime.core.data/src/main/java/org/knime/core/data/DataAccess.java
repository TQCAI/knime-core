package org.knime.core.data;

import org.knime.core.data.access.ReadAccess;
import org.knime.core.data.access.WriteAccess;

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

	// TODO interface?
	/**
	 * @return read access on values of the data
	 */
	ReadAccess read();

	/**
	 * @return write access on values of data
	 */
	WriteAccess write();
}
