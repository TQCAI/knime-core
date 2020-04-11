package org.knime.core.data.array;

import org.knime.core.data.chunk.ReadableData;

class ReadableArrayData<V extends Array<?>> implements ReadableData<V> {

	private V m_array;
	private long m_valueCount;

	// Read case
	public ReadableArrayData(V array) {
		m_array = array;
	}

	@Override
	public V get() {
		return m_array;
	}

	@Override
	public long getValueCount() {
		return m_valueCount;
	}

	@Override
	public void close() throws Exception {
		// TODO semantics
	}
}
