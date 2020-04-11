package org.knime.core.data;

public interface Data<T> {
	T get();

	long getValueCount();

	long getCapacity();

	void setValueCount(long numValues);
}
