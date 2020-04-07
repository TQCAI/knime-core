package org.knime.core.data.array;

import org.knime.core.data.array.types.Array;
import org.knime.core.data.partition.ReadablePartition;

public class ReadableNativePartition<V extends Array<?>> implements ReadablePartition<V> {

	private final V m_array;
	private final long m_size;
	private final long m_index;

	// Read case
	public ReadableNativePartition(V array, long index, long size) {
		m_array = array;
		m_index = index;
		m_size = size;
	}

	@Override
	public void close() throws Exception {
		m_array.close();
	}

	@Override
	public long size() {
		return m_size;
	}

	@Override
	public V get() {
		return m_array;
	}

	@Override
	public long getPartitionIndex() {
		return m_index;
	}
}
