package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;

interface ColumnStore<T extends DataChunk<?>, V extends DataChunkAccess<?>> extends AutoCloseable {

	/**
	 * @return linked access for this data.
	 */
	V createDataAccess();

	/**
	 * Adds data back to store. Data can then be accessed by cursor.
	 * 
	 * @param data
	 */
	void addData(T data);

	/**
	 * @return new data.
	 */
	T createData();

	/**
	 * @return cursor over all added data.
	 */
	DataChunkCursor<T> cursor();
}
