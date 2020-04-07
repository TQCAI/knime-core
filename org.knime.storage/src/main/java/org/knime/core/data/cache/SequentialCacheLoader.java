
package org.knime.core.data.cache;

import java.io.IOException;

import org.knime.core.data.partition.ReadablePartition;

public interface SequentialCacheLoader<T> extends AutoCloseable {

	ReadablePartition<T> load(long index) throws IOException;
}
