package org.knime.core.data;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.column.Domain;
import org.knime.core.data.column.ReadableColumn;
import org.knime.core.data.column.WritableColumn;

public class TableUtils {

	public static WritableTable createWritableColumnTable(final TableStore store) {
		return new WritableTable() {
			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}

			@Override
			public WritableColumn<?> getWritableColumn(long columnIndex) {
				return new DefaultWritableColumn<>(store.getStore(columnIndex));
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
				return new DefaultReadableColumn<>(store.getStore(columnIndex), store.getDomain(columnIndex));
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
				return store.getDomain(index);
			}
		};
	}

	public static TableStore cache(TableStore store) {
		return new TableStore() {

			@Override
			public <T> ColumnStore<DataChunk<T>, DataChunkAccess<T>> getStore(long index) {
				return new CachedColumnStore<T>(store.getStore(index));
			}

			@Override
			public long getNumColumns() {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Domain getDomain(long index) {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}
}
