package org.knime.core.data.record;

import org.knime.core.data.DataFactory;
import org.knime.core.data.column.ColumnType;

public interface RecordFactory extends DataFactory<Record> {
	ColumnType<?, ?>[] getColumnTypes();
}
