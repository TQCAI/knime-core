package org.knime.core.data.array.types;

import org.knime.core.data.api.column.Cursor;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.api.column.access.DoubleReadAccess;
import org.knime.core.data.api.column.domain.NumericDomain;
import org.knime.core.data.array.ArrayReadCursor;
import org.knime.core.data.array.ArrayReadStore;
import org.knime.core.data.array.types.DoubleArray.DoubleArrayAccess;

public class DoubleReadColumn implements ReadColumn<DoubleReadAccess> {

	private ArrayReadStore<DoubleArray> m_store;

	public DoubleReadColumn(ArrayReadStore<DoubleArray> store) {
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
