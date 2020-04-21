package org.knime.core.data.record;

import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnDataAccess;
import org.knime.core.data.column.ColumnType;

public final class RecordUtils {

	private RecordUtils() {
	}

	// TODO cast OK?
	@SuppressWarnings("unchecked")
	public static RecordAccess createAccess(final ColumnType<?, ?>[] m_types) {
		final ColumnDataAccess<ColumnData>[] accesses = new ColumnDataAccess[m_types.length];
		for (int i = 0; i < accesses.length; i++) {
			accesses[i] = (ColumnDataAccess<ColumnData>) m_types[i].createAccess();
		}
		return new RecordAccess(accesses);
	}
}
