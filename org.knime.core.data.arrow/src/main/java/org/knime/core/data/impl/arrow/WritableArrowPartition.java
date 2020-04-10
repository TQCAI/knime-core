package org.knime.core.data.impl.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.partition.WritablePartition;

public class WritableArrowPartition<V extends FieldVector> implements WritablePartition<V> {

	private final V m_vector;
	private final long m_capacity;
	private final long m_index;

	// write case
	public WritableArrowPartition(V vector, final long capacity, final long index) {
		m_vector = vector;
		m_capacity = capacity;
		m_index = index;
	}

	@Override
	public long getCapacity() {
		return m_capacity;
	}

	@Override
	public V get() {
		return m_vector;
	}

	@Override
	public long getPartitionIndex() {
		return m_index;
	}
}
