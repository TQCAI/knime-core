package org.knime.core.data.record;

import org.knime.core.data.DataAccess;
import org.knime.core.data.access.ReadAccess;
import org.knime.core.data.access.WriteAccess;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnDataAccess;
import org.knime.core.data.row.RowReadAccess;
import org.knime.core.data.row.RowWriteAccess;

public class RecordAccess implements DataAccess<Record>, RowWriteAccess, RowReadAccess {

	private ColumnDataAccess<ColumnData>[] m_accesses;

	public RecordAccess(ColumnDataAccess<ColumnData>[] accesses) {
		m_accesses = accesses;
	}

	public ColumnDataAccess<ColumnData>[] getAccesses() {
		return m_accesses;
	}

	public void fwd() {
		// TODO could we optimize this method call away?
		for (int i = 0; i < m_accesses.length; i++) {
			m_accesses[i].fwd();
		}
	}

	// TODO even if only called on 'chunk-switch': Can we get rid of the
	// 'record.getData()' method call
	public void update(final Record record) {
		final ColumnData[] data = record.getData();
		for (int i = 0; i < m_accesses.length; i++) {
			m_accesses[i].update(data[i]);
		}
	}

	public void reset() {
		for (int i = 0; i < m_accesses.length; i++) {
			m_accesses[i].reset();
		}
	}

	@Override
	public ReadAccess getReadAccess(int index) {
		return m_accesses[index];
	}

	@Override
	public WriteAccess getWriteAccess(int index) {
		return m_accesses[index];
	}
}
