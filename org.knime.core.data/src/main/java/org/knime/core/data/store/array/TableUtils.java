package org.knime.core.data.store.array;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.store.array.types.DoubleReadColumn;
import org.knime.core.data.store.array.types.DoubleWriteColumn;

public class TableUtils {

	public WriteTable createWriteTable(final TableStore store, final ArrayFactory factory) {
		return new WriteTable() {

			@Override
			public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
				return createWriteColumn(store.getColumnType(columnIndex), store.getColumnStore(columnIndex), factory);
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}
		};
	}

	public ReadTable createReadTable(final TableStore store) {
		return new ReadTable() {
			@Override
			public ReadColumn<?> getReadableColumn(long columnIndex) {
				return createReadColumn(store.getColumnType(columnIndex), store.getColumnStore(columnIndex));
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}
		};
	}

	private WriteColumn<? extends WritableAccess> createWriteColumn(ColumnType type, ColumnStore<?> columnStore,
			ArrayFactory factory) {
		// TODO nested
		switch (type.getPrimitiveTypes()[0]) {
		case BOOLEAN:
			return null;
		case DOUBLE:
			return new DoubleWriteColumn(() -> factory.createDoubleArray(), cast(columnStore));
		case STRING:
			return null;
		default:
			throw new IllegalArgumentException("Unknown column type!");
		}
	}

	private ReadColumn<? extends ReadableAccess> createReadColumn(ColumnType type, ColumnStore<?> columnStore) {
		// TODO nested
		switch (type.getPrimitiveTypes()[0]) {
		case BOOLEAN:
			return null;
		case DOUBLE:
			return new DoubleReadColumn(cast(columnStore));
		case STRING:
			return null;
		default:
			throw new IllegalArgumentException("Unknown column type!");
		}
	}

	private <A extends Array> ColumnStore<A> cast(ColumnStore<?> columnStore) {
		@SuppressWarnings("unchecked")
		final ColumnStore<A> store = (ColumnStore<A>) columnStore;
		return store;
	}
}
