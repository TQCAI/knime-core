package org.knime.core.data.row;

import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordReadStore;
import org.knime.core.data.record.RecordReaderHints;
import org.knime.core.data.record.RecordUtils;
import org.knime.core.data.record.RecordWriter;

public class RowTableUtils {

	public static RowWriteTable createRowWriteTable(RecordFactory factory, RecordWriter writer) {
		return new RowWriteTable() {

			private final ColumnType<?, ?>[] types = factory.getColumnTypes();

			@Override
			public int getNumColumns() {
				return types.length;
			}

			@Override
			public RowWriteCursor cursor() {
				return new RowWriteCursor(factory, writer, RecordUtils.createAccess(types));
			}
		};
	}

	public static RowReadTable createRowReadTable(ColumnType<?, ?>[] types, RecordReadStore store,
			RecordReaderHints config) {
		return new RowReadTable() {

			@Override
			public long getNumColumns() {
				return types.length;
			}

			@Override
			public RowReadCursor createReadCursor() {
				return new RowReadCursor(store.createReader(), RecordUtils.createAccess(types), config);
			}
		};
	}
}
