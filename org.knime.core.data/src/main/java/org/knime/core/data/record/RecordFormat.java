package org.knime.core.data.record;

import org.knime.core.data.DataFactory;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnDataAccess;
import org.knime.core.data.column.ColumnType;

public interface RecordFormat extends AutoCloseable {

	RecordStore create(final ColumnType<?, ?>[] types, RecordStoreHints hints);

	<D extends ColumnData, A extends ColumnDataAccess<D>> DataFactory<D> createFactory(final ColumnType<D, A> factory);

}
