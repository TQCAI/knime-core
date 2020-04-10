package org.knime.core.data.array;

import org.knime.core.data.column.ReadableAccess;
import org.knime.core.data.partition.PartitionStore;
import org.knime.core.data.partition.PartitionValue;

public interface ArrayPartitionStore<F, V extends ReadableAccess & PartitionValue<V>> extends PartitionStore<F, V> {
	void flush() throws Exception;
}
