package org.knime.core.data.data.table;

import java.util.function.Supplier;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadAccess;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.TableColumnReadAccess;
import org.knime.core.data.api.column.TableColumnWriteAccess;
import org.knime.core.data.api.column.WriteAccess;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.api.row.RowWriteCursor;
import org.knime.core.data.api.row.TableRowWriteAccess;
import org.knime.core.data.data.ConsumingDataStore;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.DataReadCursor;
import org.knime.core.data.data.DataRecordWriteCursor;
import org.knime.core.data.data.DataWriteCursor;
import org.knime.core.data.data.LoadingDataStore;

public class TableUtils {

	// Strong assumption is that cached datastore delivers the right data
	// TODO get rid of 'TableDataFactory' in this method...
	public static TableColumnWriteAccess createColumnarTableWriteAccess(ConsumingDataStore store,
			final TableDataFactory factories) {
		return new TableColumnWriteAccess() {
			private final NativeType<?, ?>[] m_primitiveTypes = store.getColumnTypes();

			@Override
			public WriteColumn<? extends WriteAccess> getWriteColumn(long columnIndex) {
				final NativeType<Data, DataAccess<Data>> cast = cast(m_primitiveTypes[(int) columnIndex]);
				return writeColumn(factories.getFactory(cast), store.getConsumer(columnIndex), cast);
			}

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}
		};
	}

	// Strong assumption is that cached data-store delivers the right data
	// TODO get rid of 'TableDataFactory' in this method...
	public static TableRowWriteAccess createRowTableWriteAccess(ConsumingDataStore store,
			final TableDataFactory factories) {
		return new TableRowWriteAccess() {
			private final NativeType<?, ?>[] m_primitiveTypes = store.getColumnTypes();

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}

			@Override
			public RowWriteCursor getRowCursor() {
				return new DataRecordWriteCursor(store, factories);
			}
		};
	}

	// Strong assumption is that cached datastore delivers the right data
	public static TableColumnReadAccess create(LoadingDataStore store) {
		return new TableColumnReadAccess() {
			private final NativeType<?, ?>[] m_primitiveTypes = store.getColumnTypes();

			@Override
			public ReadColumn<? extends ReadAccess> getReadColumn(long columnIndex) {
				return readColumn(() -> store.createLoader(columnIndex), cast(m_primitiveTypes[(int) columnIndex]));
			}

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}
		};
	}

	public static <D extends Data, A extends DataAccess<D>> ReadColumn<A> readColumn(Supplier<DataLoader<D>> reader,
			NativeType<D, A> access) {
		return new ReadColumn<A>() {

			@Override
			public Cursor<? extends A> cursor() {
				return new DataReadCursor<D, A>(reader.get(), access.createAccess());
			}
		};
	}

	public static <D extends Data, A extends DataAccess<D>> WriteColumn<A> writeColumn(DataFactory<D> factory,
			final DataConsumer<D> consumer, NativeType<D, A> access) {
		return new WriteColumn<A>() {
			@Override
			public Cursor<? extends A> access() {
				return new DataWriteCursor<>(factory, consumer, access.createAccess());
			}
		};
	}

	private static <D extends Data, A extends DataAccess<D>> NativeType<D, A> cast(NativeType<?, ?> type) {
		@SuppressWarnings("unchecked")
		final NativeType<D, A> access = (NativeType<D, A>) type;
		return access;
	}
}
