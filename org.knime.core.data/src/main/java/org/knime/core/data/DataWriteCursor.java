package org.knime.core.data;

public class DataWriteCursor<D extends Data, A extends DataAccess<D>> implements Cursor<A> {

	private final DataWriter<D> m_writer;
	private final DataFactory<D> m_factory;
	private final A m_access;

	private D m_currentData;

	private long m_currentDataMaxIndex;
	private int m_index;

	public DataWriteCursor(final DataFactory<D> factory, final DataWriter<D> writer, final A access) {
		m_writer = writer;
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
		releaseCurrentData();
		m_currentData = m_factory.create();
		m_access.update(m_currentData);
		m_currentDataMaxIndex = m_currentData.getMaxCapacity() - 1;
	}

	private void releaseCurrentData() {
		m_currentData.setNumValues(m_index);
		m_writer.write(m_currentData);
		m_currentData.release();
	}
}