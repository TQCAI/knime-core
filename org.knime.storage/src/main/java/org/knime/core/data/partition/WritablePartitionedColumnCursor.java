
package org.knime.core.data.partition;

import org.knime.core.data.table.column.WritableColumnCursor;

public final class WritablePartitionedColumnCursor<T> implements WritableColumnCursor {

	private final PartitionValue<T> m_linkedValue;

	private WritablePartition<T> m_currentPartition;

	private long m_currentPartitionMaxIndex = -1;

	private long m_index = -1;

	private PartitionStore<T> m_store;

	public WritablePartitionedColumnCursor(final PartitionStore<T> store) {
		m_linkedValue = store.createValue();
		m_store = store;
		switchToNextPartition();
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentPartitionMaxIndex) {
			switchToNextPartition();
			m_index = 0;
		}
		m_linkedValue.incIndex();
	}

	private void switchToNextPartition() {
		try {
			m_store.addForReading(m_currentPartition, m_index - 1);
			m_currentPartition = m_store.createPartition();
			m_linkedValue.updateStorage(m_currentPartition.get());
			m_currentPartitionMaxIndex = m_currentPartition.getCapacity() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public PartitionValue<T> getValue() {
		return m_linkedValue;
	}
}
