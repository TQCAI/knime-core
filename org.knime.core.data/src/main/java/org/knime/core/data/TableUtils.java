package org.knime.core.data;

import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.Record;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordReadStore;
import org.knime.core.data.record.RecordReaderConfig;
import org.knime.core.data.record.RecordUtils;
import org.knime.core.data.row.RowReadAccess;
import org.knime.core.data.row.RowReadTable;
import org.knime.core.data.row.RowWriteAccess;
import org.knime.core.data.row.RowWriteTable;

public class TableUtils {
	public static RowWriteTable createRowWriteTable(RecordFactory factory, DataWriter<Record> consumer) {
		return new RowWriteTable() {
			private final ColumnType<?, ?>[] types = factory.getColumnTypes();

			@Override
			public long getNumColumns() {
				return types.length;
			}

			@Override
			public Cursor<? extends RowWriteAccess> getRowCursor() {
				return new DataWriteCursor<>(factory, consumer, RecordUtils.createAccess(types));
			}
		};
	}

	public static RowReadTable createRowReadTable(ColumnType<?, ?>[] types, RecordReadStore store,
			RecordReaderConfig config) {
		return new RowReadTable() {
			@Override
			public long getNumColumns() {
				return types.length;
			}

			@Override
			public Cursor<? extends RowReadAccess> createRowReadCursor() {
				return new DataReadCursor<>(store.createReader(config), RecordUtils.createAccess(types));
			}
		};
	}
}
