package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.DataWriter;

public interface TableData extends AutoCloseable {

	<D extends Data> DataWriter<D> getWriter(long index);

	<D extends Data> DataLoader<D> createLoader(long index);

	<D extends Data> DataFactory<D> getFactory(long index);

	long getNumColumns();

}
