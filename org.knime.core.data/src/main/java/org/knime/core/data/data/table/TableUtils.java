package org.knime.core.data.data.table;

import java.util.Map;
import java.util.function.Supplier;

import org.knime.core.data.api.PrimitiveType;
import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteableTable;
import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadAccess;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.api.column.domain.Domain;
import org.knime.core.data.data.ConsumingDataStore;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataChunkReadCursor;
import org.knime.core.data.data.DataChunkWriteCursor;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.LoadingDataStore;

public class TableUtils {

	// Strong assumption is that cached datastore delivers the right data
	// TODO get rid of 'TableDataFactory' in this class...
	public static WriteableTable create(ConsumingDataStore store, final TableDataFactory factories) {
		return new WriteableTable() {
			private final PrimitiveType<?, ?>[] m_primitiveTypes = store.getPrimitiveSpec();

			@Override
			public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
				return writeColumn(factories.getFactory(columnIndex), store.getConsumer(columnIndex),
						cast(m_primitiveTypes[(int) columnIndex]));
			}

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}
		};
	}

	// Strong assumption is that cached datastore delivers the right data
	public static ReadTable create(LoadingDataStore store) {
		return new ReadTable() {
			private final PrimitiveType<?, ?>[] m_primitiveTypes = store.getPrimitiveSpec();

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
			PrimitiveType<D, A> access) {
		return new ReadColumn<A>() {

			@Override
			public Cursor<? extends A> cursor() {
				return new DataChunkReadCursor<D, A>(reader.get(), access.createAccess());
			}
		};
	}

	public static <D extends Data, A extends DataAccess<D>> WriteColumn<A> writeColumn(DataFactory<D> factory,
			final DataConsumer<D> consumer, PrimitiveType<D, A> access) {
		return new WriteColumn<A>() {
			@Override
			public Cursor<? extends A> access() {
				return new DataChunkWriteCursor<>(factory, consumer, access.createAccess());
			}
		};
	}

	private static <D extends Data, A extends DataAccess<D>> PrimitiveType<D, A> cast(PrimitiveType<?, ?> type) {
		@SuppressWarnings("unchecked")
		final PrimitiveType<D, A> access = (PrimitiveType<D, A>) type;
		return access;
	}
}
