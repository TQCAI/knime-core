package org.knime.core.data;

import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.Record;
import org.knime.core.data.record.RecordAccess;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordReadStore;
import org.knime.core.data.record.RecordReaderHints;
import org.knime.core.data.record.RecordUtils;
import org.knime.core.data.row.RowReadTable;
import org.knime.core.data.row.RowWriteTable;

public class TableUtils {
	public static RowWriteTable createRowWriteTable(RecordFactory factory, DataWriter<Record> consumer) {
		return new RowWriteTable() {

			private final ColumnType<?, ?>[] types = factory.getColumnTypes();

			private final DataWriteCursor<Record, RecordAccess> cursor = new DataWriteCursor<Record, RecordAccess>(factory, consumer,
					RecordUtils.createAccess(types));

			@Override
			public long getNumColumns() {
				return types.length;
			}

			@Override
			public void close() throws Exception {
				// TODO Auto-generated method stub

			}
		};
	}

	public static RowReadTable createRowReadTable(ColumnType<?, ?>[] types, RecordReadStore store,
			RecordReaderHints config) {
		return new RowReadTable() {
			private final DataReadCursor<Record, RecordAccess> cursor = new DataReadCursor<>(store.createReader(config),
					RecordUtils.createAccess(types));
			
			
			
			@Override
			public long getNumColumns() {
				return types.length;
			}
		};
	}
}
