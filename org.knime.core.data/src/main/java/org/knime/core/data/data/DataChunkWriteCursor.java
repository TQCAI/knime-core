package org.knime.core.data.data;

import org.knime.core.data.api.column.Cursor;

public class DataChunkWriteCursor<A extends Data, T extends DataAccess<A>> implements Cursor<T> {

	private final DataConsumer<A> m_consumer;
	private final DataFactory<A> m_factory;

	private A m_currentArray;
	private T m_access;

	private long m_currentDataMaxIndex;
	private long m_index;
	private int m_currentIndex = 0;

	public DataChunkWriteCursor(final DataFactory<A> factory, final DataConsumer<A> consumer, final T access) {
		switchToNextArray();
		m_consumer = consumer;
		m_factory = factory;
		m_access = access;
	}

	private void switchToNextArray() {
		try {
			releaseCurrentData();
			m_currentArray = m_factory.create();
			m_access.updateStorage(m_currentArray);
			m_currentDataMaxIndex = m_currentArray.getMaxCapacity() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentArray.setNumValues(m_index);
		m_consumer.accept(m_currentIndex++, m_currentArray);
		m_currentArray.release();
	}

	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextArray();
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