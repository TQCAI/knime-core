package org.knime.core.data.store;

public interface Data<T extends DataAccess<T>> extends ReferencedData<T> {

	/**
	 * @return the underlying data object
	 */
	T get();

	/**
	 * @return maxium number of values which can be set.
	 */
	long getMaxCapacity();

	/**
	 * @param number of values actually set
	 */
	void setNumValues(long count);

	/**
	 * number of actual values
	 */
	long getNumValues();
	
	

}
