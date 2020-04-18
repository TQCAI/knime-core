package org.knime.core.data.array.types;

import java.util.function.Supplier;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.api.column.access.DoubleWriteAccess;
import org.knime.core.data.array.ArrayWriteCursor;
import org.knime.core.data.array.ArrayWriteStore;
import org.knime.core.data.array.types.DoubleArray.DoubleArrayAccess;

public class DoubleWriteColumn implements WriteColumn<DoubleWriteAccess> {

	private Supplier<DoubleArray> m_supplier;
	private ArrayWriteStore<DoubleArray> m_store;

	public DoubleWriteColumn(Supplier<DoubleArray> supplier, ArrayWriteStore<DoubleArray> store) {
		m_supplier = supplier;
		m_store = store;
	}

	@Override
	public Cursor<? extends DoubleWriteAccess> access() {
		return new ArrayWriteCursor<>(m_supplier, m_store, new DoubleArrayAccess());
	}

}
