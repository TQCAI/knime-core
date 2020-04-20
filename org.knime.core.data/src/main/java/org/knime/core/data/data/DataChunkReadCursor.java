package org.knime.core.data.data;

import org.knime.core.data.api.column.Cursor;

public class DataChunkReadCursor<A extends Data, T extends DataAccess<A>> implements Cursor<T> {

	private final DataLoader<A> m_loader;

	private long m_arrayIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Data m_currentArray;
	private T m_access;

	public DataChunkReadCursor(DataLoader<A> loader, T access) {
		switchToNextArray();
		m_loader = loader;
		m_access = access;
	}

	private void switchToNextArray() {
		try {
			releaseCurrentData();
			m_currentArray = m_loader.load(m_arrayIndex++);
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
		return m_index < m_currentDataMaxIndex || m_arrayIndex < m_loader.size();
	}
}