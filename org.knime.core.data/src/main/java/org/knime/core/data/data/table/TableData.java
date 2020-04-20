package org.knime.core.data.data.table;

import org.knime.core.data.api.PrimitiveType;

// TODO revise interface hierarchy
public interface TableData extends AutoCloseable {

	TableDataReadAccess getReadAccess();

	TableDataWriteAccess getWriteAccess();

	TableDataFactory getFactory();

	long getNumColumns();

	PrimitiveType<?, ?>[] getPrimitiveSpec();

}
