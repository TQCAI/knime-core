package org.knime.core.data.array;

import org.knime.core.data.partition.PartitionStore;

public interface ArrayPartitionStore<F> extends PartitionStore<F> {
	void flush() throws Exception;
}
