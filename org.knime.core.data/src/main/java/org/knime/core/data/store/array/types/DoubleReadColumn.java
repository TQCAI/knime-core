package org.knime.core.data.store.array.types;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.access.DoubleReadAccess;
import org.knime.core.data.api.column.domain.NumericDomain;
import org.knime.core.data.store.array.ArrayReadCursor;
import org.knime.core.data.store.array.ColumnStore;
import org.knime.core.data.store.array.types.DoubleArray.DoubleArrayAccess;

public class DoubleReadColumn implements ReadColumn<DoubleReadAccess> {

	private ColumnStore<DoubleArray> m_store;

	public DoubleReadColumn(ColumnStore<DoubleArray> store) {
		m_store = store;
	}

	@Override
	public NumericDomain getDomain() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Cursor<? extends DoubleReadAccess> cursor() {
		return new ArrayReadCursor<>(m_store, new DoubleArrayAccess());
	}

}
