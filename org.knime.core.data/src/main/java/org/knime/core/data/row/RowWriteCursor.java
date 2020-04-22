package org.knime.core.data.row;

import org.knime.core.data.record.Record;
import org.knime.core.data.record.RecordAccess;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordWriter;
import org.knime.core.data.value.WriteValue;

//TODO similar logic required later for columnar access...
//TODO interface
public class RowWriteCursor {

	private final RecordWriter m_writer;
	private final RecordFactory m_factory;
	private final RecordAccess m_access;

	private Record m_currentData;
	private long m_currentDataMaxIndex;
	private int m_index;

	public RowWriteCursor(final RecordFactory factory, final RecordWriter writer, final RecordAccess access) {
		m_writer = writer;
		m_factory = factory;
		m_access = access;

		switchToNextData();
	}

	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	public WriteValue get(int index) {
		return m_access.write(index);
	}

	public void close() throws Exception {
		releaseCurrentData();
	}

	private void switchToNextData() {
		releaseCurrentData();
		m_currentData = m_factory.create();
		m_access.update(m_currentData);
		m_currentDataMaxIndex = m_currentData.getMaxCapacity() - 1;
	}

	private void releaseCurrentData() {
		m_currentData.setNumValues(m_index);
		m_writer.write(m_currentData);
		m_currentData.release();
	}
}