package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataLoader;

public interface TableDataReadAccess {
	<D extends Data> DataLoader<D> createLoader(long index);
}
