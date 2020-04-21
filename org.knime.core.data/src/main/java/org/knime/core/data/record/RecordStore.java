package org.knime.core.data.record;

import org.knime.core.data.column.ColumnType;
import org.knime.core.data.store.DataStore;

public interface RecordStore extends RecordWriteStore, RecordReadStore, DataStore<Record, RecordReaderConfig> {

	ColumnType<?, ?>[] getColumnTypes();
}
