package org.knime.core.data.array;

import java.util.List;
import java.util.function.Consumer;

import org.knime.core.data.chunk.ReadableData;
import org.knime.core.data.chunk.WritableData;

class WritableArrayData<V extends Array<?>> implements ReadableData<V> {

	private V m_array;
	private long m_valueCount;

	// Read case
	public WritableArrayData(V array, Consumer<WritableArrayData<V>> finishedWriting) {
		m_array = array;
		m_finishedWriting = finishedWriting;
	}

	@Override
	public V get() {
		return m_array;
	}

	@Override
	public long getCapacity() {
		return m_array.size();
	}

	@Override
	public void setValueCount(long numValues) {
		m_valueCount = numValues;
	}

	@Override
	public long getValueCount() {
		return m_valueCount;
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub

	}
}
