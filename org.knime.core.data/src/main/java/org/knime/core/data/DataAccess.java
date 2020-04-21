package org.knime.core.data;

public interface DataAccess<D> {

	/**
	 * Updates underlying data.
	 * 
	 * @param data
	 */
	void update(D data);

	void fwd();

	void reset();
}
