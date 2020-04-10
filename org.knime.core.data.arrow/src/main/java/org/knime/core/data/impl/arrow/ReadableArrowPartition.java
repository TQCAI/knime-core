package org.knime.core.data.impl.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.partition.ReadablePartition;

public class ReadableArrowPartition<V extends FieldVector> implements ReadablePartition<V> {

	private final V m_vector;
	private final long m_index;
	private long m_numEntries;

	// Read case
	public ReadableArrowPartition(V vector, long index, long numEntries) {
		m_vector = vector;
		m_index = index;
		m_numEntries = numEntries;
	}

	@Override
	public void close() throws Exception {
		m_vector.close();
	}

	@Override
	public V get() {
		return m_vector;
	}

	@Override
	public long getPartitionIndex() {
		return m_index;
	}

	@Override
	public long size() {
		return m_numEntries;
	}
}
