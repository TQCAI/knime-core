package org.knime.core.data;

public interface DataAccess<T> {

	void update(final T data);

	void incIndex();

}
