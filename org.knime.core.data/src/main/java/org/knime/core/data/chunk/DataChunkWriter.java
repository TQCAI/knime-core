package org.knime.core.data.chunk;

public interface DataChunkWriter<T> extends AutoCloseable {

	void write(long index, DataChunk<T> data);

}
