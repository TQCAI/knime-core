package org.knime.core.data.array.table;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.array.Array;
import org.knime.core.data.array.ArrayFactory;
import org.knime.core.data.array.ArrayReadStore;
import org.knime.core.data.array.ArrayWriteStore;
import org.knime.core.data.array.types.DoubleReadColumn;
import org.knime.core.data.array.types.DoubleWriteColumn;

public class TableUtils {

	public WriteTable createWriteTable(final WriteTable store, final ArrayFactory factory) {
		return new WriteTable() {

			@Override
			public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
				return createWriteColumn(store.getColumnType(columnIndex), store.getColumnWriteStore(columnIndex),
						factory);
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}
		};
	}

	public ReadTable createReadTable(final DefaultReadTable store) {
		return new ReadTable() {
			@Override
			public ReadColumn<?> getReadableColumn(long columnIndex) {
				return createReadColumn(store.getColumnType(columnIndex), store.getColumnReadStore(columnIndex));
			}

			@Override
			public long getNumColumns() {
				return store.getNumColumns();
			}
		};
	}

	private WriteColumn<? extends WritableAccess> createWriteColumn(ColumnType type, ArrayFactory factory) {
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

	private ReadColumn<? extends ReadableAccess> createReadColumn(ColumnType type, ArrayReadStore<?> columnStore) {
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

	private <A extends Array> ArrayReadStore<A> cast(ArrayReadStore<?> columnStore) {
		@SuppressWarnings("unchecked")
		final ArrayReadStore<A> store = (ArrayReadStore<A>) columnStore;
		return store;
	}

	private <A extends Array> ArrayWriteStore<A> cast(ArrayWriteStore<?> columnStore) {
		@SuppressWarnings("unchecked")
		final ArrayWriteStore<A> store = (ArrayWriteStore<A>) columnStore;
		return store;
	}
}
