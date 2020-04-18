
package org.knime.core.data.api.row;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.ColumnReadCursor;

public final class ColumnBackedReadableRow implements ReadableRowCursor {

	public static ColumnBackedReadableRow fromReadableTable(final ReadTable table) {
		final List<ColumnReadCursor<?>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			ReadColumn<?> col = table.getReadableColumn(i);
			columns.addCache(col.cursor());
		}
		return new ColumnBackedReadableRow(columns);
	}

	private final List<ColumnReadCursor<?>> m_columns;
	private final List<ReadableAccess> m_dataValues;

	public ColumnBackedReadableRow(final List<ColumnReadCursor<?>> columns) {
		m_columns = columns;
		m_dataValues = new ArrayList<>(columns.size());
		for (final ColumnReadCursor<?> column : m_columns) {
			m_dataValues.addCache(column.get());
		}
	}

	@Override
	public boolean canFwd() {
		return !m_columns.isEmpty() && m_columns.get(0).canFwd();
	}

	@Override
	public void fwd() {
		for (final ColumnReadCursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public ReadableAccess getReadableAccess(final long index) {
		return m_dataValues.get((int) index);
	}

	@Override
	public void close() throws Exception {
		for (final ColumnReadCursor<?> column : m_columns) {
			column.close();
		}
	}
}
