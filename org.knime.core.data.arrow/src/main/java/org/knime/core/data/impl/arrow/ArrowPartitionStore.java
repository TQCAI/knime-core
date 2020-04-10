package org.knime.core.data.impl.arrow;

import org.knime.core.data.partition.PartitionStore;

public interface ArrowPartitionStore<F> extends PartitionStore<F> {
	void flush() throws Exception;
}
