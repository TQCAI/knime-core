package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;
import org.knime.core.data.column.Domain;

public interface ColumnStore<T, D extends DataChunk<T>, V extends DataChunkAccess<T>> extends AutoCloseable {

	Domain getDomain();

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
	DataChunkCursor<T, D> cursor();
}
