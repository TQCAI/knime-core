package org.knime.core.data.row;

import org.knime.core.data.Data;
import org.knime.core.data.record.RecordAccess;
import org.knime.core.data.record.RecordReader;
import org.knime.core.data.record.RecordReaderHints;
import org.knime.core.data.value.ReadValue;

//TODO similar logic required later for columnar access...
//TODO interface
public final class RowReadCursor {

	private final RecordReader m_reader;
	private final RecordAccess m_access;

	private int m_dataIndex = 0;
	private int m_currentDataMaxIndex;
	private int m_index;

	private Data m_currentData;
	private RecordReaderHints m_hints;

	public RowReadCursor(final RecordReader reader, final RecordAccess access, RecordReaderHints hints) {
		m_reader = reader;
		m_access = access;
		m_hints = hints;

		switchToNextData();
	}

	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	public ReadValue get(int index) {
		return m_access.read(index);
	}

	public void close() throws Exception {
		closeCurrentData();
	}

	public boolean canFwd() {
		return m_index < m_currentDataMaxIndex || m_dataIndex < m_reader.getNumChunks();
	}

	private void switchToNextData() {
		try {
			releaseCurrentData();
			m_currentData = m_reader.read(m_dataIndex++, m_hints);
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