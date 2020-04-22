package org.knime.core.data.record;

import org.knime.core.data.Data;
import org.knime.core.data.column.ColumnData;

// TODO interface?
public final class Record implements Data {

	private ColumnData[] m_data;

	private int m_numValues;

	public Record(ColumnData... data) {
		m_data = data;
	}

	// TODO share interface with nested types
	public ColumnData[] getData() {
		return m_data;
	}

	@Override
	public int getMaxCapacity() {
		return m_data[0].getMaxCapacity();
	}

	@Override
	public void setNumValues(int numValues) {
		m_numValues = numValues;
	}

	@Override
	public void release() {
		for (int i = 0; i < m_data.length; i++) {
			m_data[i].release();
		}
	}

	@Override
	public void retain() {
		for (int i = 0; i < m_data.length; i++) {
			m_data[i].retain();
		}
	}

	@Override
	public int getNumValues() {
		return m_numValues;
	}

}
