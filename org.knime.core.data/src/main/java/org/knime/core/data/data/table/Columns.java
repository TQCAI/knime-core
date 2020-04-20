package org.knime.core.data.data.table;

import java.util.function.Supplier;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.api.column.domain.Domain;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataChunkReadCursor;
import org.knime.core.data.data.DataChunkWriteCursor;
import org.knime.core.data.data.DataConsumer;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.types.DoubleData;
import org.knime.core.data.data.types.DoubleData.DoubleAccess;

public final class Columns {

	public static WriteColumn<DoubleAccess> writeDoubles(DataFactory<DoubleData> factory,
			DataConsumer<DoubleData> writer) {
		// TODO do we want to create a 'DoubleColumn' here?
		return writeColumn(factory, writer, new DoubleAccess());
	}

	public static ReadColumn<DoubleAccess> readDoubles(Supplier<DataLoader<DoubleData>> reader, Domain domain) {
		// TODO do we want to create a double column here?
		return readColumn(reader, domain, new DoubleAccess());
	}

	private static <D extends Data, A extends DataAccess<D>> ReadColumn<A> readColumn(Supplier<DataLoader<D>> reader,
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

	private static <D extends Data, A extends DataAccess<D>> WriteColumn<A> writeColumn(DataFactory<D> factory,
			final DataConsumer<D> consumer, A access) {
		return new WriteColumn<A>() {
			@Override
			public Cursor<? extends A> access() {
				return new DataChunkWriteCursor<>(factory, consumer, access);
			}
		};
	}
}
