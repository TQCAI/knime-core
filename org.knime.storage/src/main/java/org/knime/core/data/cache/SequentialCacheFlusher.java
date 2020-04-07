
package org.knime.core.data.cache;

import java.io.IOException;

import org.knime.core.data.partition.ReadablePartition;

public interface SequentialCacheFlusher<O> extends AutoCloseable {

	void flush(ReadablePartition<O> obj) throws IOException;
}
