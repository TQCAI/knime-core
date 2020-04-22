package org.knime.core.data.record;

public interface RecordReader {

	Record read(int chunkIndex, RecordReaderHints hints);

	int getNumChunks();
}
