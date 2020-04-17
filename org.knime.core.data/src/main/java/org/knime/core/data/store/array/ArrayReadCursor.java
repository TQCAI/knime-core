package org.knime.core.data.store.array;

public class ArrayReadCursor<A extends Array, T extends ArrayAccess<A>> implements ArrayCursor<T> {

	private long m_arrayIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Array m_currentArray;
	private T m_access;
	private ColumnStore<A> m_store;

	public ArrayReadCursor(ColumnStore<A> store, T access) {
		switchToNextArray();
		m_store = store;
		m_access = access;
	}

	private void switchToNextArray() {
		try {
			releaseCurrentData();
			m_currentArray = m_store.get(m_arrayIndex++);
			m_currentDataMaxIndex = m_currentArray.getNumValues() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentArray.release();
	}

	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextArray();
			m_index = 0;
		}
		m_access.fwd();
	}

	public T get() {
		return m_access;
	}

	@Override
	public void close() throws Exception {
		closeCurrentPartition();
	}

	private void closeCurrentPartition() throws Exception {
		if (m_currentArray != null) {
			m_currentArray.release();
		}
	}

	@Override
	public boolean canFwd() {
		return m_index < m_currentDataMaxIndex || m_arrayIndex < m_store.size();
	}
}