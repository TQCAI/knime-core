package org.knime.core.data.array;

import java.io.IOException;
import java.util.function.Supplier;

import org.knime.core.data.array.types.Array;
import org.knime.core.data.cache.SequentialCache;
import org.knime.core.data.partition.PartitionValue;
import org.knime.core.data.partition.ReadablePartition;
import org.knime.core.data.partition.WritablePartition;

class DefaultArrayPartitionStore<T extends Array<?>> implements ArrayPartitionStore<T> {

	private final Supplier<PartitionValue<T>> m_valueSupplier;
	private final Supplier<T> m_partitionSupplier;
	private final SequentialCache<T> m_cache;
	private long m_partitionIdx;

	public DefaultArrayPartitionStore(Supplier<PartitionValue<T>> partitionValueSupplier,
			Supplier<T> partitionSupplier, SequentialCache<T> cache) {
		m_valueSupplier = partitionValueSupplier;
		m_partitionSupplier = partitionSupplier;
		m_cache = cache;
	}

	@Override
	public PartitionValue<T> createValue() {
		return m_valueSupplier.get();
	}

	// TODO abstract this into cache.
	@Override
	public WritablePartition<T> createPartition() {
		T t = m_partitionSupplier.get();
		return new WritableArrayPartition<T>(t, m_partitionIdx++, t.size());
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
		m_cache.add(
				new ReadableNativePartition<T>(partition.get(), partition.getPartitionIndex(), numValuesWritten));
	}

}