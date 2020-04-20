package org.knime.core.data.data;

import org.knime.core.data.api.column.Cursor;

public class DataChunkReadCursor<D extends Data, T extends DataAccess<D>> implements Cursor<T> {

	private final DataLoader<D> m_loader;

	private long m_dataIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Data m_currentData;
	private T m_access;

	public DataChunkReadCursor(DataLoader<D> loader, T access) {
		switchToNextArray();
		m_loader = loader;
		m_access = access;
	}

	private void switchToNextArray() {
		try {
			releaseCurrentData();
			m_currentData = m_loader.load(m_dataIndex++);
			m_currentDataMaxIndex = m_currentData.getNumValues() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentData.release();
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
		if (m_currentData != null) {
			m_currentData.release();
		}
	}

	@Override
	public boolean canFwd() {
		return m_index < m_currentDataMaxIndex || m_dataIndex < m_loader.size();
	}
}