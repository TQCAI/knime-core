package org.knime.core.data.data;

import org.knime.core.data.api.column.Cursor;

/*
* TODO at the moment we have a lot of redundant checks/logic per column (e.g. when to load the next chunk of data, forward etc).
* We could do this on a table level for each column synchronously.
*/
public class DataWriteCursor<D extends Data, T extends DataAccess<D>> implements Cursor<T> {

	private final DataConsumer<D> m_consumer;
	private final DataFactory<D> m_factory;

	private D m_currentData;
	private T m_access;

	private long m_currentDataMaxIndex;
	private int m_index;

	public DataWriteCursor(final DataFactory<D> factory, final DataConsumer<D> consumer, final T access) {
		m_consumer = consumer;
		m_factory = factory;
		m_access = access;

		switchToNextData();
	}

	private void switchToNextData() {
		try {
			releaseCurrentData();
			m_currentData = m_factory.create();
			m_access.update(m_currentData);
			m_currentDataMaxIndex = m_currentData.getMaxCapacity() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentData.setNumValues(m_index);
		m_consumer.accept(m_currentData);
		m_currentData.release();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	public T get() {
		return m_access;
	}

	@Override
	public void close() throws Exception {
		releaseCurrentData();
	}

	@Override
	public boolean canFwd() {
		return true;
	}
}