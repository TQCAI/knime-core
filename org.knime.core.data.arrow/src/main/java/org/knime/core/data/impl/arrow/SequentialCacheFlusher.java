
package org.knime.core.data.impl.arrow;

import java.io.IOException;

import org.knime.core.data.partition.ReadablePartition;

public interface SequentialCacheFlusher<O> extends AutoCloseable {

	void flush(ReadablePartition<O> obj) throws IOException;
}
