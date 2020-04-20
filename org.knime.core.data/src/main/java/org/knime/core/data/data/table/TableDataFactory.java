package org.knime.core.data.data.table;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataFactory;

public interface TableDataFactory {
	<D extends Data, A extends DataAccess<D>> DataFactory<D> getFactory(NativeType<D, A> type);
}
