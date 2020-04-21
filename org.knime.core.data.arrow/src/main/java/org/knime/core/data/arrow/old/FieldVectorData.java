package org.knime.core.data.arrow.old;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.Data;

class FieldVectorData<V extends FieldVector> implements Data<V> {

	private final V m_vector;
	private final long m_maxCapacity;

	// write case
	public FieldVectorData(final V vector, long maxCapacity) {
		m_vector = vector;
		m_maxCapacity = maxCapacity;
	}

	@Override
	public long getCapacity() {
		return m_maxCapacity;
	}

	@Override
	public V get() {
		return m_vector;
	}

	@Override
	public void setNumValues(long numValues) {
		m_vector.setValueCount((int) numValues);
	}

	@Override
	public long getValueCount() {
		return m_vector.getValueCount();
	}
}
