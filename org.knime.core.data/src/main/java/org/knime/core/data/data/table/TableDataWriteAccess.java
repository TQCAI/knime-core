package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataWriter;

public interface TableDataWriteAccess {
	<D extends Data> DataWriter<D> getWriter(long index);
}
