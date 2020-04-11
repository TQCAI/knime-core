package org.knime.core.data.array;

import org.knime.core.data.store.Data;

class ArrayData<V extends Array<?>> implements Data<V> {

	private V m_array;
	private long m_valueCount;

	// Read case
	public ArrayData(V array) {
		m_array = array;
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
}
