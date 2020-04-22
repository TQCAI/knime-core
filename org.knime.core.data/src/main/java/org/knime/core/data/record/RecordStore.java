package org.knime.core.data.record;

import org.knime.core.data.column.ColumnType;

public interface RecordStore extends RecordWriteStore, RecordReadStore {

	ColumnType<?, ?>[] getColumnTypes();
}
