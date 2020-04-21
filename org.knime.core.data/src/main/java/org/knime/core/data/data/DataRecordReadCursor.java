package org.knime.core.data.data;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadAccess;

/*
* TODO at the moment we have a lot of redundant checks/logic per column (e.g. when to load the next chunk of data, forward etc).
* We could do this on a table level for each column synchronously.
*/
public class DataRecordReadCursor implements Cursor<ReadAccess[]> {

	// TODO hide all of that in a 'DataRecord' object? Would be additional,
	// unnecessary method calls.
	// TODO DataRecord would also help ensuring that all data have same capacity
	private final DataLoader<Data>[] m_loaders;
	private final DataAccess<Data>[] m_accesses;
	private final int m_length;

	private long m_dataIndex = 0;
	private long m_currentDataMaxIndex;
	private long m_index;

	private Data[] m_currentData;

	@SuppressWarnings("unchecked")
	public DataRecordReadCursor(LoadingDataStore store) {
		NativeType<?, ?>[] columnTypes = store.getColumnTypes();
		m_length = columnTypes.length;
		m_loaders = new DataLoader[m_length];
		m_accesses = new DataAccess[m_length];
		m_currentData = new Data[m_length];

		for (int i = 0; i < columnTypes.length; i++) {
			m_loaders[i] = store.createLoader(i);
			m_accesses[i] = (DataAccess<Data>) columnTypes[i].createAccess();
		}

		switchToNextData();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}

		// TODO no idea how to circumvent this.
		for (int i = 0; i < m_length; i++) {
			m_accesses[i].fwd();
		}
	}

	@Override
	public ReadAccess[] get() {
		return m_accesses;
	}

	@Override
	public void close() throws Exception {
		closeCurrentData();
	}

	@Override
	public boolean canFwd() {
		// loaders[0] required as we don't have a 'DataRecord'. DataRecord would also
		// ensure all data have same length.
		return m_index < m_currentDataMaxIndex || m_dataIndex < m_loaders[0].size();
	}

	private void closeCurrentData() throws Exception {
		if (m_currentData != null) {
			releaseCurrentData();
		}
	}

	private void switchToNextData() {
		try {
			releaseCurrentData();
			for (int i = 0; i < m_length; i++) {
				m_currentData[i] = m_loaders[i].load(m_dataIndex);
			}
			m_currentDataMaxIndex = m_currentData[0].getNumValues() - 1;
			m_dataIndex++;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		for (int i = 0; i < m_length; i++) {
			m_currentData[i].release();
		}
	}
}