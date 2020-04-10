package org.knime.core.data.impl.arrow.struct;
//package org.knime.core.data.arrow.struct;
//
//import org.knime.core.data.partition.Partition;
//import org.knime.core.data.partition.ReadablePartition;
//
//class StructArrowPartition implements ReadablePartition<Partition<?>[]> {
//
//	private ReadablePartition<?>[] m_partitions;
//	private long m_index;
//
//	public StructArrowPartition(ReadablePartition<?>[] partitions, long index) {
//		m_partitions = partitions;
//		m_index = index;
//	}
//
//	@Override
//	public void close() throws Exception {
//		for (int i = 0; i < m_partitions.length; i++) {
//			m_partitions[i].close();
//		}
//	}
//
//	@Override
//	public ReadablePartition<?>[] get() {
//		return m_partitions;
//	}
//
//	@Override
//	public long getPartitionIndex() {
//		return m_index;
//	}
//
//	@Override
//	public long size() {
//		// Assumption: all partitions have equals size
//		return m_partitions[0].size();
//	}
//
//}
