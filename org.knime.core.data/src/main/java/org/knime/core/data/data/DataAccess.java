package org.knime.core.data.data;

import org.knime.core.data.api.column.ReadAccess;
import org.knime.core.data.api.column.WriteAccess;

public interface DataAccess<D extends Data> extends ReadAccess, WriteAccess {

	/**
	 * Updates underlying data.
	 * 
	 * @param data
	 */
	void update(D data);

	void fwd();

	void reset();
}
