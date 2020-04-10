package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;

interface ColumnStore<T, V extends DataChunkAccess<T>> extends AutoCloseable {

	/**
	 * @return linked access for this data.
	 */
	V createDataAccess();

	/**
	 * Adds data back to store. Data can then be accessed by cursor.
	 * 
	 * @param data
	 */
	void addData(DataChunk<T> data);

	/**
	 * @return new data.
	 */
	DataChunk<T> createData();

	/**
	 * @return cursor over all added data.
	 */
	DataChunkCursor<T> cursor();
}
