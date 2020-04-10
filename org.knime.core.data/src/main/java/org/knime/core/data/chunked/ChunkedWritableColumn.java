package org.knime.core.data.chunked;

import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;
import org.knime.core.data.column.Domain;
import org.knime.core.data.column.WritableAccess;
import org.knime.core.data.column.WritableColumn;
import org.knime.core.data.column.WritableCursor;

public class ChunkedWritableColumn<T, V extends WritableAccess & DataAccess<T>, D extends Domain>
		implements WritableColumn<V> {

	private ChunkedDataStore<T, Data<T>, V> m_store;

	public ChunkedWritableColumn(final ChunkedDataStore<T, Data<T>, V> store) {
		m_store = store;
	}

	@Override
	public WritableCursor<V> createWritableCursor() {
		return new WritableCursor<V>() {

			private final V m_value = m_store.createDataAccess();

			private Data<T> m_currentData;
			private long m_currentDataMaxIndex = -1;
			private long m_index = -1;

			{
				switchToNextData();
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
					m_currentData = m_store.createData();
					m_value.update(m_currentData.get());
					m_currentDataMaxIndex = m_currentData.getCapacity() - 1;
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
				if (m_currentData != null) {
					m_currentData.setValueCount(m_index);
					m_currentData.close();
				}
			}

			@Override
			public boolean canFwd() {
				return false;
			}
		};
	}
}
