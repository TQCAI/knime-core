package org.knime.core.data.store;

import java.util.Iterator;

import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.ColumnReadCursor;

class ReadableDataStoreAccess<T, A extends ReadableAccess & DataAccess<T>> implements ColumnReadCursor<A> {

	private final Iterator<Data<T>> m_it;
	private final A m_access;

	private long m_currentDataMaxIndex = -1;
	private long m_index = -1;

	private Data<T> m_currentData;

	ReadableDataStoreAccess(final DataStore<T, A> datastore) {
		m_it = datastore.iterator();
		m_access = datastore.createAccess();
		// initialize first partition
		switchToNextData();
	}

	@Override
	public boolean canFwd() {
		return m_index < m_currentDataMaxIndex || m_it.hasNext();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	private void switchToNextData() {
		try {
			closeCurrentPartition();
			m_currentData = m_it.next();
			m_access.set(m_currentData.get());
			m_currentDataMaxIndex = m_currentData.getNumValues() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public A get() {
		return m_access;
	}

	@Override
	public void close() throws Exception {
		closeCurrentPartition();
	}

	private void closeCurrentPartition() throws Exception {
		if (m_currentData != null) {
			m_currentData.release();
		}
	}
}
