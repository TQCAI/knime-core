
package org.knime.core.data.api.row;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadAccess;
import org.knime.core.data.api.column.ReadColumn;

public final class ColumnBackedReadableRow implements ReadableRowCursor {

	public static ColumnBackedReadableRow fromReadableTable(final ReadTable table) {
		final List<Cursor<? extends ReadAccess>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			ReadColumn<?> col = table.getReadColumn(i);
			columns.add(col.cursor());
		}
		return new ColumnBackedReadableRow(columns);
	}

	private final List<Cursor<? extends ReadAccess>> m_columns;
	private final List<ReadAccess> m_dataValues;

	public ColumnBackedReadableRow(final List<Cursor<? extends ReadAccess>> columns) {
		m_columns = columns;
		m_dataValues = new ArrayList<>(columns.size());
		for (final Cursor<? extends ReadAccess> column : m_columns) {
			m_dataValues.add(column.get());
		}
	}

	@Override
	public boolean canFwd() {
		return !m_columns.isEmpty() && m_columns.get(0).canFwd();
	}

	@Override
	public void fwd() {
		for (final Cursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public ReadAccess getReadableAccess(final long index) {
		return m_dataValues.get((int) index);
	}

	@Override
	public void close() throws Exception {
		for (final Cursor<?> column : m_columns) {
			column.close();
		}
	}
}
