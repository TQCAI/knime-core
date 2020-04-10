
package org.knime.core.data.table;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.column.ReadableCursor;

//TODO: Implemented against KNIME classes ('DataValue', 'DataCell', ...)
public final class ColumnBackedReadableRow implements ReadableRow {

	public static ColumnBackedReadableRow fromReadableTable(final ReadableTable table) {
		final List<ReadableCursor<?>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			columns.add(table.getReadableColumn(i).createReadableCursor());
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
	public long getNumColumns() {
		return m_dataValues.size();
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
	public ReadableAccess getReadableAccess(final long idx) {
		return m_dataValues.get((int) idx);
	}

	@Override
	public void close() throws Exception {
		for (final ReadableCursor<?> column : m_columns) {
			column.close();
		}
	}
}
