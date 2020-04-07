package org.knime.core.data.partition;

public interface Partition<T> {
	long getPartitionIndex();

	T get();
}
