
package org.knime.core.data.api.row;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.WriteableTable;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.WritableAccess;

public final class ColumnBackedWritableRow implements WritableRow {

	public static ColumnBackedWritableRow fromWritableTable(final WriteableTable table) {
		final List<Cursor<? extends WritableAccess>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			columns.add(table.getWritableColumn(i).access());
		}
		return new ColumnBackedWritableRow(columns);
	}

	private final List<Cursor<? extends WritableAccess>> m_columns;

	public ColumnBackedWritableRow(final List<Cursor<? extends WritableAccess>> columns) {
		m_columns = columns;
	}

	@Override
	public void fwd() {
		for (final Cursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public WritableAccess getWritableAccess(final long index) {
		return m_columns.get((int) index).get();
	}

	@Override
	public void close() throws Exception {
		for (final Cursor<?> column : m_columns) {
			column.close();
		}
	}
}
