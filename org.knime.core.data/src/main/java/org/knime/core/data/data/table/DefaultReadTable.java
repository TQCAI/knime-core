package org.knime.core.data.data.table;

import java.util.ArrayList;
import java.util.Map;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.domain.Domain;

//TODO interface / abstract super class for read/write
class DefaultReadTable implements ReadTable {

	// TODO long...
	private ArrayList<ReadColumn<?>> m_columns;

	public DefaultReadTable(ArrayList<ReadColumn<?>> columns, Map<Long, Domain> domains) {
		m_columns = columns;
	}

	@Override
	public long getNumColumns() {
		return m_columns.size();
	}

	@Override
	public ReadColumn<?> getReadColumn(long columnIndex) {
		return m_columns.get((int) columnIndex);
	}
}
