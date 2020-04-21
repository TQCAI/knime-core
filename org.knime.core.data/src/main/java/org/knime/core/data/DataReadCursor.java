package org.knime.core.data;

/*
* TODO at the moment we have a lot of redundant checks/logic per column (e.g. when to load the next chunk of data, forward etc).
* We could do this on a table level for each column synchronously.
*/
public class DataReadCursor<D extends Data, T extends DataAccess<D>> implements Cursor<T> {

	private final DataReader<D> m_loader;

	private long m_dataIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Data m_currentData;
	private T m_access;

	public DataReadCursor(DataReader<D> loader, T access) {
		m_loader = loader;
		m_access = access;

		switchToNextData();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	@Override
	public T get() {
		return m_access;
	}

	@Override
	public void close() throws Exception {
		closeCurrentData();
	}

	@Override
	public boolean canFwd() {
		return m_index < m_currentDataMaxIndex || m_dataIndex < m_loader.size();
	}

	private void switchToNextData() {
		try {
			releaseCurrentData();
			m_currentData = m_loader.read(m_dataIndex++);
			m_currentDataMaxIndex = m_currentData.getNumValues() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentData.release();
	}

	private void closeCurrentData() throws Exception {
		if (m_currentData != null) {
			releaseCurrentData();
		}
	}
}