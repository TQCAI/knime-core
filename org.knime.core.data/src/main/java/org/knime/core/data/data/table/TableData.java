package org.knime.core.data.data.table;

import org.knime.core.data.api.NativeType;

// TODO revise interface hierarchy
public interface TableData extends AutoCloseable {

	TableDataReadAccess getReadAccess();

	TableDataWriteAccess getWriteAccess();

	TableDataFactory getFactory();

	long getNumColumns();

	NativeType<?, ?>[] getColumnTypes();

}
