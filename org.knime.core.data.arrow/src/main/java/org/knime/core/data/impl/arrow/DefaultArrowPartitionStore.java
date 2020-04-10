package org.knime.core.data.impl.arrow;

import java.io.IOException;
import java.util.function.Supplier;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.CachedDataStore;
import org.knime.core.data.partition.PartitionValue;
import org.knime.core.data.partition.ReadablePartition;
import org.knime.core.data.partition.WritablePartition;

class DefaultArrowPartitionStore<T extends FieldVector> implements ArrowPartitionStore<T> {

	private final Supplier<PartitionValue<T>> m_valueSupplier;
	private final Supplier<T> m_partitionSupplier;
	private final CachedDataStore<T> m_cache;
	private long m_partitionIdx;

	public DefaultArrowPartitionStore(Supplier<PartitionValue<T>> partitionValueSupplier, Supplier<T> partitionSupplier,
			CachedDataStore<T> cache) {
		m_valueSupplier = partitionValueSupplier;
		m_partitionSupplier = partitionSupplier;
		m_cache = cache;
	}

	@Override
	public PartitionValue<T> createLinkedValue() {
		return m_valueSupplier.get();
	}

	@Override
	public WritableArrowPartition<T> createPartition() {
		final T t = m_partitionSupplier.get();
		return new WritableArrowPartition<T>(t, t.getValueCapacity(), m_partitionIdx++);
	}

	@Override
	public ReadablePartition<T> getReadablePartition(long index) throws IOException {
		return m_cache.get(index);
	}

	@Override
	public long getNumPartitions() {
		return m_partitionIdx;
	}

	@Override
	public void flush() throws Exception {
		// TODO is there a difference between 'flush' and 'releaseMemory'
		m_cache.flush();
		m_cache.clear();
	}

	// release all memory
	@Override
	public void close() throws Exception {
		m_cache.close();
	}

	@Override
	public void addForReading(WritablePartition<T> partition, long numValuesWritten) {
		m_cache.add(new ReadableArrowPartition<>(partition.get(), partition.getPartitionIndex(), numValuesWritten));
	}

}