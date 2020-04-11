package org.knime.core.data.array;

public class AbstractNativeArray<A> implements Array<A> {

	private long[] m_isMissing;
	private A m_array;
	private int m_capacity;

	protected AbstractNativeArray(A array, int capacity) {
		m_capacity = capacity;
		m_array = array;
		m_isMissing = new long[((int) capacity / 64) + 1];
	}

	@Override
	public boolean isMissing(long index) {
		// NB: inspired by imglib2
		return 1 == ((m_isMissing[((int) index >>> 6)] >>> ((index & 63))) & 1l);
	}

	@Override
	public void setMissing(long index) {
		// NB: inspired by imglib
		final int i1 = (int) index >>> 6;
		m_isMissing[i1] = m_isMissing[i1] | 1l << (index & 63);
	}

	public A get() {
		return m_array;
	}

	@Override
	public long size() {
		return m_capacity;
	}

}
