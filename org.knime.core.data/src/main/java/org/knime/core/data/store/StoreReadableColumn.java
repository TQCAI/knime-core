package org.knime.core.data.store;

import org.knime.core.data.api.column.ReadableAccess;
import org.knime.core.data.api.column.ReadableColumn;
import org.knime.core.data.api.column.ReadableCursor;
import org.knime.core.data.api.column.domain.Domain;

class StoreReadableColumn<T, V extends ReadableAccess & DataAccess<T>, D extends Domain>
		implements ReadableColumn<V, D> {

	private DataStore<T, V> m_store;
	private D m_domain;

	public StoreReadableColumn(final DataStore<T, V> store, D domain) {
		m_store = store;
		m_domain = domain;
	}

	@Override
	public D getDomain() {
		return m_domain;
	}

	@Override
	public ReadableCursor<V> createReadableCursor() {
		return new ReadableCursor<V>() {

			private DataCursor<T> m_cursor = m_store.cursor();
			private final V m_value = m_store.createDataAccess();
			private long m_currentDataMaxIndex = -1;
			private long m_index = -1;

			private Data<T> m_currentData;
			{
				switchToNextData();
			}

			@Override
			public boolean canFwd() {
				return m_index < m_currentDataMaxIndex || m_cursor.canFwd();
			}

			@Override
			public void fwd() {
				if (++m_index > m_currentDataMaxIndex) {
					switchToNextData();
					m_index = 0;
				}
				m_value.incIndex();
			}

			private void switchToNextData() {
				try {
					closeCurrentPartition();
					m_cursor.fwd();
					m_currentData = m_cursor.get();
					m_value.update(m_currentData.get());
					m_currentDataMaxIndex = m_currentData.getValueCount() - 1;
				} catch (final Exception e) {
					// TODO
					throw new RuntimeException(e);
				}
			}

			@Override
			public V get() {
				return m_value;
			}

			@Override
			public void close() throws Exception {
				closeCurrentPartition();
			}

			private void closeCurrentPartition() throws Exception {
				if (m_currentData != null) {
					m_cursor.close();
				}
			}
		};
	}

}
