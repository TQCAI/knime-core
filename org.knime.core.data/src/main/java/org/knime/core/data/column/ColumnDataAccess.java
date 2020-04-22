package org.knime.core.data.column;

import org.knime.core.data.DataAccess;
import org.knime.core.data.value.ReadValue;
import org.knime.core.data.value.WriteValue;

public interface ColumnDataAccess<D extends ColumnData> extends DataAccess<D> {

	// TODO interface?
	/**
	 * @return read access on values of the data
	 */
	ReadValue read();

	/**
	 * @return write access on values of data
	 */
	WriteValue write();
}
