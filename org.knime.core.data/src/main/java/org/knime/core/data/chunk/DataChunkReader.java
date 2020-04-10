package org.knime.core.data.chunk;

public interface DataChunkReader<T> extends AutoCloseable {

	DataChunk<T> readChunk(long index);

}
