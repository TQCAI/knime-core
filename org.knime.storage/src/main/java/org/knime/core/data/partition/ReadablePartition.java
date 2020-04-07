package org.knime.core.data.partition;

public interface ReadablePartition<T> extends Partition<T>, AutoCloseable {

	long size();
}
