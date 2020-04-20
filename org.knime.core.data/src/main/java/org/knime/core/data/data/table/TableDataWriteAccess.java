package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;

public interface TableDataWriteAccess {
	<D extends Data> ColumnBatchWriter<D> getWriter();
}
