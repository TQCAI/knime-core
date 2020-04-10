package org.knime.core.data;

public interface Data<T> extends AutoCloseable {
	T get();

	long getCapacity();

	void setValueCount(long numValues);

	long getValueCount();
}
