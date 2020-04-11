package org.knime.core.data.store;

public interface Data<T> {
	T get();

	long getValueCount();

	long getCapacity();

	void setValueCount(long numValues);
}
