
package org.knime.core.data.api.row;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.WriteAccess;

public final class ColumnBackedWritableRow implements WritableRow {

	public static ColumnBackedWritableRow fromWritableTable(final WriteTable table) {
		final List<Cursor<? extends WriteAccess>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			columns.add(table.getWriteColumn(i).access());
		}
		return new ColumnBackedWritableRow(columns);
	}

	private final List<Cursor<? extends WriteAccess>> m_columns;

	public ColumnBackedWritableRow(final List<Cursor<? extends WriteAccess>> columns) {
		m_columns = columns;
	}

	@Override
	public void fwd() {
		for (final Cursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public WriteAccess getWritableAccess(final long index) {
		return m_columns.get((int) index).get();
	}

	@Override
	public void close() throws Exception {
		for (final Cursor<?> column : m_columns) {
			column.close();
		}
	}
}
