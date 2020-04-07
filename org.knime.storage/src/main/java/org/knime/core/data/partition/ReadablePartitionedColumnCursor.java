
package org.knime.core.data.partition;

import org.knime.core.data.table.column.ReadableColumnCursor;

public final class ReadablePartitionedColumnCursor<T> implements ReadableColumnCursor {

	private final PartitionValue<T> m_linkedAccess;

	private ReadablePartition<T> m_currentPartition;

	private long m_currentPartitionMaxIndex = -1;

	private long m_index = -1;

	private long m_partitionIndex = -1;

	private PartitionStore<T> m_store;

	public ReadablePartitionedColumnCursor(final PartitionStore<T> store) {
		m_linkedAccess = store.createValue();
		m_store = store;
		switchToNextPartition();
	}

	@Override
	public boolean canFwd() {
		return m_index < m_currentPartitionMaxIndex || m_partitionIndex < m_store.getNumPartitions() - 1;
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentPartitionMaxIndex) {
			switchToNextPartition();
			m_index = 0;
		}
		m_linkedAccess.incIndex();
	}

	private void switchToNextPartition() {
		try {
			m_partitionIndex++;
			m_currentPartition = m_store.getReadablePartition(m_partitionIndex);
			m_linkedAccess.updateStorage(m_currentPartition.get());
			m_currentPartitionMaxIndex = m_currentPartition.size() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public PartitionValue<T> getValue() {
		return m_linkedAccess;
	}
}
