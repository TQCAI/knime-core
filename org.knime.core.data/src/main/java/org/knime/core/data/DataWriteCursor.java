package org.knime.core.data;

public class DataWriteCursor<D extends Data, A extends DataAccess<D>> implements Cursor<A> {

	private final DataWriter<D> m_consumer;
	private final DataFactory<D> m_factory;

	private D m_currentData;
	private A m_access;

	private long m_currentDataMaxIndex;
	private int m_index;

	public DataWriteCursor(final DataFactory<D> factory, final DataWriter<D> consumer, final A access) {
		m_consumer = consumer;
		m_factory = factory;
		m_access = access;

		switchToNextData();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}
		m_access.fwd();
	}

	@Override
	public A get() {
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
		m_consumer.write(m_currentData);
		m_currentData.release();
	}
}