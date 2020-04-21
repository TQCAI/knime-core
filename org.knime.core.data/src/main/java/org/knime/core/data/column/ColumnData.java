package org.knime.core.data.column;

import org.knime.core.data.Data;

public interface ColumnData extends Data {

	/**
	 * @param set value missing at index. Default is false.
	 */
	void setMissing(int index);

	/**
	 * @param index of value
	 * @return true, if value is missing. Default is false.
	 */
	boolean isMissing(int index);
}
