package org.knime.core.data.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.store.Data;

class FieldVectorData<V extends FieldVector> implements Data<V> {

	private final V m_vector;

	// write case
	public FieldVectorData(final V vector) {
		m_vector = vector;
	}

	@Override
	public long getCapacity() {
		return m_vector.getValueCapacity();
	}

	@Override
	public V get() {
		return m_vector;
	}

	@Override
	public void setValueCount(long numValues) {
		m_vector.setValueCount((int) numValues);
	}

	@Override
	public long getValueCount() {
		return m_vector.getValueCount();
	}
}
