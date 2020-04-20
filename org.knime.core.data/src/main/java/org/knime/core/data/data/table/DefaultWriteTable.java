package org.knime.core.data.data.table;

import java.util.List;

import org.knime.core.data.api.WriteableTable;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;

//TODO interface / abstract super class for read/write
public class DefaultWriteTable implements WriteableTable {

	// TODO long...
	private List<WriteColumn<?>> m_columns;

	public DefaultWriteTable(final List<WriteColumn<?>> columns) {
		m_columns = columns;
	}

	@Override
	public long getNumColumns() {
		return m_columns.size();
	}

	@Override
	public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
		return m_columns.get((int) columnIndex);
	}
}
