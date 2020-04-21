package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.table.TableDataReadAccess.DataBatchLoader;

public interface TableDataWriteAccess {
	<D extends Data> DataBatchLoader<D> getWriter();

	interface DataBatchWriter<D> {
		void write(int columnIndex, Data data);

		void flush();
	}

	interface DataBatchWriter2 {
		void write(Data[] data);
	}
}
