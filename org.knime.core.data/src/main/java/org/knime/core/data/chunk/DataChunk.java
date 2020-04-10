package org.knime.core.data.chunk;

public interface DataChunk<T> extends AutoCloseable {
	T get();

	long getCapacity();

	void setValueCount(long numValues);

	long getValueCount();
}
