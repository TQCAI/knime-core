
package org.knime.core.data;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.column.ReadableColumn;
import org.knime.core.data.column.ReadableCursor;

final class ColumnBackedReadableRow implements ReadableRowCursor {

	public static ColumnBackedReadableRow fromReadableTable(final ReadableTable table) {
		final List<ReadableCursor<?>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			ReadableColumn<?, ?> col = table.getReadableColumn(i);
			columns.add(col.createReadableCursor());
		}
		return new ColumnBackedReadableRow(columns);
	}

	private final List<ReadableCursor<?>> m_columns;
	private final List<ReadableAccess> m_dataValues;

	public ColumnBackedReadableRow(final List<ReadableCursor<?>> columns) {
		m_columns = columns;
		m_dataValues = new ArrayList<>(columns.size());
		for (final ReadableCursor<?> column : m_columns) {
			m_dataValues.add(column.get());
		}
	}

	@Override
	public boolean canFwd() {
		return !m_columns.isEmpty() && m_columns.get(0).canFwd();
	}

	@Override
	public void fwd() {
		for (final ReadableCursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public ReadableAccess getReadableAccess(final long index) {
		return m_dataValues.get((int) index);
	}

	@Override
	public void close() throws Exception {
		for (final ReadableCursor<?> column : m_columns) {
			column.close();
		}
	}
}
