
package org.knime.core.data;

import org.knime.core.data.column.WritableAccess;
import org.knime.core.data.column.WritableColumn;
import org.knime.core.data.column.WritableCursor;

class DefaultWritableColumn<T, V extends WritableAccess & DataAccess<T>> implements WritableColumn<V> {

	private final ColumnDataStore<T, V> m_store;

	public DefaultWritableColumn(final ColumnDataStore<T, V> store) {
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
					returnCurrentData();
					m_currentData = m_store.create();
					m_value.update(m_currentData.get());
					m_currentDataMaxIndex = m_currentData.getCapacity() - 1;
				} catch (final Exception e) {
					// TODO
					throw new RuntimeException(e);
				}
			}

			private void returnCurrentData() throws Exception {
				if (m_currentData != null) {
					m_currentData.setValueCount(m_index);
					// TODO Finished writing
					m_store.store(m_currentData);
				}
			}

			@Override
			public V get() {
				return m_value;
			}

			@Override
			public void close() throws Exception {
				returnCurrentData();
			}

			@Override
			public boolean canFwd() {
				return true;
			}
		};
	}
}
