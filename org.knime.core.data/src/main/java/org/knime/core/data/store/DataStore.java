package org.knime.core.data.store;

import org.knime.core.data.api.column.domain.Domain;

public interface DataStore<T, V extends DataAccess<T>> extends AutoCloseable {

	/**
	 * @return domain for this store
	 */
	UpdatableDomain<T> getDomain();

	/**
	 * @return linked access for this data.
	 */
	V createDataAccess();

	/**
	 * Append it to list of all data. Data can be accessed via DataCursor
	 * subsequently to store.
	 * 
	 * @param data to be stored.
	 */
	void add(Data<T> data);

	/**
	 * @return a cursor over all stored data.
	 */
	DataCursor<T> cursor();

	/**
	 * Creates new data. Not added to store.
	 * 
	 * @return new data.
	 */
	Data<T> create();

	/**
	 * Offers the DataStore data to free memory. If data has not been stored
	 * previously, data may be lost.
	 * 
	 * @param data to release.
	 */
	void release(Data<T> data);

	/**
	 * @param data stored and released data.
	 */
	default void addAndRelease(Data<T> data) {
		add(data);
		release(data);
	}

	/**
	 * Closes store, i.e. no further elements can be 'stored'. Subsequent calls to
	 * 'store' will fail.
	 */
	void closeForWriting();
}
