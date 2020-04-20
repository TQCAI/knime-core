package org.knime.core.data.data;

import org.knime.core.data.api.Referenced;

public interface Data extends Referenced {

	/**
	 * @param set value missing at index. Default is false.
	 */
	void setMissing(int index);

	/**
	 * @param index of value
	 * @return true, if value is missing. Default is false.
	 */
	boolean isMissing(int index);

	/**
	 * @return maximum capacity of an array
	 */
	int getMaxCapacity();

	/**
	 * @return number of values set
	 */
	int getNumValues();

	/**
	 * @param numValues set number of written values
	 */
	void setNumValues(int numValues);
}
