package org.knime.core.data.data.table;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataFactory;

public interface TableDataFactory {
	<A extends Data> DataFactory<A> getFactory(long index);
}
