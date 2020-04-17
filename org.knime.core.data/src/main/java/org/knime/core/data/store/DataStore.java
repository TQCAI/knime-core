package org.knime.core.data.store;

import java.util.Iterator;

import org.knime.core.data.api.column.PrimitiveType;

public interface DataStore<T, A extends DataAccess<T>> extends Iterable<Data<T>>, AutoCloseable {

	/**
	 * @return type of store.
	 */
	PrimitiveType type();

	/**
	 * @return access on data
	 */
	A createAccess();

	/**
	 * @return new data
	 */
	Data<T> create();

	/**
	 * @return the data. retains data before return.
	 */
	Data<T> get(long index);

	/**
	 * @return size
	 */
	long size();

	/**
	 * Append it to list of all data. Data can be accessed via DataCursor after
	 * adding the Data object to the store.
	 * 
	 * @param data to be stored.
	 */
	void add(Data<T> data);

	/**
	 * Closes the store for write. All persisted data will remain.
	 */
	void closeForConsume();

	/**
	 * Closes store. Removes all traces in memory.
	 */
	@Override
	void close() throws Exception;

	/**
	 * Destroys the entire store. All data associated with the store will be
	 * destroyed.
	 * 
	 * @throws Exception
	 */
	void destroy() throws Exception;

	@Override
	default Iterator<Data<T>> iterator() {
		return new Iterator<Data<T>>() {

			private long m_index = 0;

			@Override
			public boolean hasNext() {
				return m_index < size();
			}

			@Override
			public Data<T> next() {
				final Data<T> data = get(m_index++);
				data.retain();
				return data;
			}
		};
	}
}
