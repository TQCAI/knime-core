package org.knime.core.data.partition;

import java.io.IOException;

public interface PartitionStore<T> extends AutoCloseable {

	// create linked value
	PartitionValue<T> createValue();

	// create a new partition. not managed by store.
	WritablePartition<T> createPartition();

	// get partition at index
	ReadablePartition<T> getReadablePartition(long index) throws IOException;

	// adds a partition back to store. Partition can be accessed afterwards as
	// ReadablePartition
	void addForReading(WritablePartition<T> partition, long numValuesWritten);

	// number managed partition
	long getNumPartitions();

}
