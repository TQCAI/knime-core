package org.knime.core.data.store;

import org.knime.core.data.api.ReadableTable;
import org.knime.core.data.api.WritableTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.ReadableColumn;
import org.knime.core.data.api.column.WritableColumn;
import org.knime.core.data.api.column.domain.Domain;
import org.knime.core.data.api.row.ColumnBackedReadableRow;
import org.knime.core.data.api.row.ColumnBackedWritableRow;
import org.knime.core.data.api.row.ReadableRowCursor;
import org.knime.core.data.api.row.ReadableRowTable;
import org.knime.core.data.api.row.WritableRow;
import org.knime.core.data.api.row.WritableRowTable;
import org.knime.core.data.store.cache.CachedTableStore;

public class TableStoreUtils {

	public static WritableTable createWritableColumnTable(final TableStore store) {
		return new WritableTable() {
			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}

			@Override
			public WritableColumn<?> getWritableColumn(long columnIndex) {
				return new StoreWritableColumn<>(store.getStore(columnIndex));
			}
		};
	}

	public static ReadableTable createReadableTable(final TableStore store) {
		return new ReadableTable() {
			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}

			@Override
			public ReadableColumn<?, ?> getReadableColumn(long columnIndex) {
				// TODO domain?
				return new StoreReadableColumn<>(store.getStore(columnIndex), null);
			}
		};
	}

	public static WritableRowTable createWritableRowTable(final TableStore store) {
		return new WritableRowTable() {

			@Override
			public WritableRow getWritableRow() {
				return ColumnBackedWritableRow.fromWritableTable(createWritableColumnTable(store));
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}
		};
	}

	public static ReadableRowTable createReadableRowTable(final TableStore store) {
		return new ReadableRowTable() {

			@Override
			public ReadableRowCursor getRowCursor() {
				return ColumnBackedReadableRow.fromReadableTable(createReadableTable(store));
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}

			@Override
			public Domain getDomain(long index) {
//				return store.getDomain(index);
				throw new UnsupportedOperationException("Not yet implemented");
			}
		};
	}

	public static CachedTableStore cache(TableStore store) {
		return new CachedTableStore(store);
	}

	public static DefaultTableStore createTableStore(DataStoreFactory factory, ColumnType... types) {
		return new DefaultTableStore(factory, types);
	}
}
