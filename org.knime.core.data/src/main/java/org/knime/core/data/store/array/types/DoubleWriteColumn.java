package org.knime.core.data.store.array.types;

import java.util.function.Supplier;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.api.column.access.DoubleWriteAccess;
import org.knime.core.data.store.array.ArrayWriteCursor;
import org.knime.core.data.store.array.ColumnStore;
import org.knime.core.data.store.array.types.DoubleArray.DoubleArrayAccess;

public class DoubleWriteColumn implements WriteColumn<DoubleWriteAccess> {

	private Supplier<DoubleArray> m_supplier;
	private ColumnStore<DoubleArray> m_store;

	public DoubleWriteColumn(Supplier<DoubleArray> supplier, ColumnStore<DoubleArray> store) {
		m_supplier = supplier;
		m_store = store;
	}

	@Override
	public Cursor<? extends DoubleWriteAccess> access() {
		return new ArrayWriteCursor<>(m_supplier, m_store, new DoubleArrayAccess());
	}

}
