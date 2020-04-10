
package org.knime.core.data;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.column.WritableAccess;
import org.knime.core.data.column.WritableCursor;

// TODO: Implemented against KNIME classes ('DataValue', 'DataCell', ...)
// TODO implement as cursor!
public final class ColumnBackedWritableRow implements WritableRow {

	public static ColumnBackedWritableRow fromWritableTable(final WritableTable table) {
		final List<WritableCursor<?>> columns = new ArrayList<>(Math.toIntExact(table.getNumColumns()));
		for (long i = 0; i < table.getNumColumns(); i++) {
			columns.add(table.getWritableColumn(i).createWritableCursor());
		}
		return new ColumnBackedWritableRow(columns);
	}

	private final List<WritableCursor<?>> m_columns;

	public ColumnBackedWritableRow(final List<WritableCursor<?>> columns) {
		m_columns = columns;
	}

	@Override
	public void fwd() {
		for (final WritableCursor<?> column : m_columns) {
			column.fwd();
		}
	}

	@Override
	public WritableAccess getWritableAccess(final long index) {
		return m_columns.get((int) index).get();
	}

	@Override
	public void close() throws Exception {
		for (final WritableCursor<?> column : m_columns) {
			column.close();
		}
	}
}
