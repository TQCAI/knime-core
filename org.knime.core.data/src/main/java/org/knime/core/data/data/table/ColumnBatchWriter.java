package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;

public interface ColumnBatchWriter<A extends Data> extends AutoCloseable {
	void accept(Data[] data);
}
