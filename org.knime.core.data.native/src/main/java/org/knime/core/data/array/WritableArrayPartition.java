package org.knime.core.data.array;

import org.knime.core.data.array.types.Array;
import org.knime.core.data.partition.WritablePartition;

public class WritableArrayPartition<V extends Array<?>> implements WritablePartition<V> {

	private final V m_array;
	private final long m_capacity;
	private final long m_index;

	// Read case
	public WritableArrayPartition(V array, long index, long capacity) {
		m_array = array;
		m_index = index;
		m_capacity = capacity;
	}

	@Override
	public V get() {
		return m_array;
	}

	@Override
	public long getPartitionIndex() {
		return m_index;
	}

	@Override
	public long getCapacity() {
		return m_capacity;
	}
}
