package org.knime.core.data.data.table;

import org.knime.core.data.api.PrimitiveType;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.DataWriter;

// TODO revise interface hierarchy
public interface TableData extends AutoCloseable, TableDataFactory {

	<D extends Data> DataWriter<D> getWriter(long index);

	<D extends Data> DataLoader<D> createLoader(long index);

	long getNumColumns();

	PrimitiveType[] getPrimitiveSpec();

}
