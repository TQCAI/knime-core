package org.knime.core.data.chunked;

import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;
import org.knime.core.data.column.Domain;
import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.column.ReadableColumn;
import org.knime.core.data.column.ReadableCursor;

public class ChunkedReadableColumn<T, V extends ReadableAccess & DataAccess<T>, D extends Domain>
		implements ReadableColumn<V, D> {

	private D m_domain;

	private ChunkedDataStore<T, Data<T>, V> m_store;

	public ChunkedReadableColumn(final ChunkedDataStore<T, Data<T>, V> store, final D domain) {
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

			private ChunkedDataCursor<T, Data<T>> m_cursor = m_store.cursor();
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
