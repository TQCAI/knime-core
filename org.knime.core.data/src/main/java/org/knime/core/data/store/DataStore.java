package org.knime.core.data.store;

public interface DataStore<T, V extends DataAccess<T>> extends AutoCloseable {

	/**
	 * @return linked access for this data.
	 */
	V createDataAccess();

	/**
	 * @param store data. makes sure that it can be read again by cursor, even if
	 *              destroyed.
	 */
	void store(Data<T> data);

	/**
	 * @return a cursor over all stored data
	 */
	DataCursor<T> cursor();

	/**
	 * @return new data.
	 */
	Data<T> create();

	/**
	 * @param releases the data from memory. It's the callers responsibility to make
	 *                 sure that no-one requires this data anymore.
	 */
	void release(Data<T> data);
}
