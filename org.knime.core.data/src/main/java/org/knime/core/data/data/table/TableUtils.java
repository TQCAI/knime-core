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
import org.knime.core.data.data.CachedDataStore;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataChunkReadCursor;
import org.knime.core.data.data.DataChunkWriteCursor;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.DataLoader;

public class TableUtils {

	// Strong assumption is that cached datastore delivers the right data
	// TODO get rid of 'TableDataFactory'
	public static WriteableTable create(CachedDataStore store, final TableDataFactory factories) {
		return new WriteableTable() {
			private final PrimitiveType[] m_primitiveTypes = store.getPrimitiveSpec();

			@Override
			public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
				return writeColumn(factories.getFactory(columnIndex), store.getConsumer(columnIndex),
						m_primitiveTypes[(int) columnIndex].access());
			}

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}
		};
	}

	// Strong assumption is that cached datastore delivers the right data
	public static ReadTable create(CachedDataStore store, final Map<Long, Domain> domains) {
		return new ReadTable() {
			private final PrimitiveType[] m_primitiveTypes = store.getPrimitiveSpec();

			@Override
			public ReadColumn<? extends ReadAccess> getReadColumn(long columnIndex) {
				return readColumn(() -> store.createLoader(columnIndex), domains.get(columnIndex),
						m_primitiveTypes[(int) columnIndex].access());
			}

			@Override
			public long getNumColumns() {
				return m_primitiveTypes.length;
			}
		};
	}

	public static <D extends Data, A extends DataAccess<D>> ReadColumn<A> readColumn(Supplier<DataLoader<D>> reader,
			Domain domain, A access) {
		return new ReadColumn<A>() {

			@Override
			public Cursor<? extends A> cursor() {
				return new DataChunkReadCursor<D, A>(reader.get(), access);
			}

			@Override
			public Domain getDomain() {
				return domain;
			}
		};
	}

	public static <D extends Data, A extends DataAccess<D>> WriteColumn<A> writeColumn(DataFactory<D> factory,
			final DataConsumer<D> consumer, A access) {
		return new WriteColumn<A>() {
			@Override
			public Cursor<? extends A> access() {
				return new DataChunkWriteCursor<>(factory, consumer, access);
			}
		};
	}
}
