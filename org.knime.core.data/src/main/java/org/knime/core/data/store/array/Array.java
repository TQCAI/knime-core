package org.knime.core.data.store.array;

public interface Array {

	/**
	 * @param set value missing at index. Default is false.
	 */
	void setMissing(long index);

	/**
	 * @param index of value
	 * @return true, if value is missing. Default is false.
	 */
	boolean isMissing(long index);

	/**
	 * @return maximum capacity of an array
	 */
	long getMaxCapacity();

	/**
	 * @return number of values set
	 */
	int getNumValues();

	/**
	 * @param numValues set number of written values
	 */
	void setNumValues(long numValues);

	/**
	 * Release reference on array
	 */
	void release();

	/**
	 * Retain reference on array
	 */
	void retain();
}
