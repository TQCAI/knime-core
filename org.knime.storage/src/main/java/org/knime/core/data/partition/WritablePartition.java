package org.knime.core.data.partition;

public interface WritablePartition<T> extends Partition<T> {

	long getCapacity();
}
