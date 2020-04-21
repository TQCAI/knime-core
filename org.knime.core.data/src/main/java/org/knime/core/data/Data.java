package org.knime.core.data;

public interface Data extends Referenced {

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
