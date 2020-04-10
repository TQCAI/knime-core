package org.knime.core.data.impl.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.chunk.DataChunk;

class FieldVectorDataChunk<V extends FieldVector> implements DataChunk<V> {

	private final V m_vector;

	// write case
	public FieldVectorDataChunk(final V vector) {
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
	public void close() throws Exception {
		m_vector.close();
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
