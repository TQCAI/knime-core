
package org.knime.core.data.api.row;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.ColumnWriteCursor;

public final class ColumnBackedWritableRow implements WritableRow {

	public static ColumnBackedWritableRow fromWritableTable(final WriteTable table) {
		final List<ColumnWriteCursor<?>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			columns.addCache(table.getWritableColumn(i).access());
		}
		return new ColumnBackedWritableRow(columns);
	}

	private final List<ColumnWriteCursor<?>> m_columns;

	public ColumnBackedWritableRow(final List<ColumnWriteCursor<?>> columns) {
		m_columns = columns;
	}

	@Override
	public void fwd() {
		for (final ColumnWriteCursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public WritableAccess getWritableAccess(final long index) {
		return m_columns.get((int) index).get();
	}

	@Override
	public void close() throws Exception {
		for (final ColumnWriteCursor<?> column : m_columns) {
			column.close();
		}
	}
}
