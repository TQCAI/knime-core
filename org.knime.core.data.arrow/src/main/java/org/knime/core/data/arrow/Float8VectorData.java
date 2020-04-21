package org.knime.core.data.arrow;

import java.util.concurrent.atomic.AtomicInteger;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.DoubleData;

public class Float8VectorData implements DoubleData {

	private AtomicInteger m_refCounter;

	private Float8Vector m_vector;
	private int m_values;

	Float8VectorData(BufferAllocator allocator, long chunkSize) {
		m_vector = new Float8Vector((String) null, allocator);
		m_vector.allocateNew((int) chunkSize);
		m_refCounter = new AtomicInteger(1);
	}

	@Override
	public double getDouble(int index) {
		return m_vector.get(index);
	}

	@Override
	public void setDouble(int index, double value) {
		m_vector.set(index, value);
	}

	@Override
	public void setMissing(int index) {
		m_vector.setNull(index);
	}

	@Override
	public boolean isMissing(int index) {
		return m_vector.isNull(index);
	}

	@Override
	public int getMaxCapacity() {
		return m_vector.getValueCapacity();
	}

	@Override
	public int getNumValues() {
		return m_values;
	}

	@Override
	public void setNumValues(int numValues) {
		m_values = numValues;
	}

	// TODO thread safety for ref-counting
	// TODO extract to super class / interface
	@Override
	public void release() {
		if (m_refCounter.decrementAndGet() == 0) {
			m_vector.clear();
		}
	}

	@Override
	public void retain() {
		m_refCounter.getAndIncrement();
	}
}
