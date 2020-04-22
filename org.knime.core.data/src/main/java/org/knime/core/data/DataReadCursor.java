package org.knime.core.data;

import org.knime.core.data.access.ReadAccess;

/*
* TODO at the moment we have a lot of redundant checks/logic per column (e.g. when to load the next chunk of data, forward etc).
* We could do this on a table level for each column synchronously.
*/
public class DataReadCursor<D extends Data, A extends DataAccess<D>> implements Cursor<ReadAccess> {

	private final DataReader<D> m_loader;
	private final ReadAccess m_readAccess;
	private final A m_access;

	private long m_dataIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Data m_currentData;

	public DataReadCursor(DataReader<D> loader, A access) {
		m_loader = loader;
		m_access = access;
		m_readAccess = access.read();

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
	public ReadAccess get() {
		return m_readAccess;
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