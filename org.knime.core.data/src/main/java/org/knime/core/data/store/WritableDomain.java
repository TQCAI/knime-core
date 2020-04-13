package org.knime.core.data.store;

import org.knime.core.data.api.column.domain.Domain;

public interface WritableDomain<T> extends Domain {

	/**
	 * Adds data to the domain.
	 * 
	 * @param data used for the updated.
	 */
	void add(T data);
}
