package org.knime.core.data.chunked;

import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;

public interface ChunkedDataStore<T, D extends Data<T>, V extends DataAccess<T>> {

	/**
	 * @return linked access for this data.
	 */
	V createDataAccess();

	/**
	 * Adds data back to store. Data can then be accessed by cursor.
	 * 
	 * @param data
	 */
	void addData(D data);

	/**
	 * @return new data.
	 */
	D createData();

	/**
	 * @return cursor over all added data.
	 */
	ChunkedDataCursor<T, D> cursor();
}
