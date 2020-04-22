package org.knime.core.data.record;

import java.io.File;

import org.knime.core.data.column.ColumnType;

public interface RecordFormat {

	RecordFactory createFactory(final ColumnType<?, ?>[] schema);

	RecordStore create(ColumnType<?, ?>[] schema, File file, RecordStoreHints hints);

}
