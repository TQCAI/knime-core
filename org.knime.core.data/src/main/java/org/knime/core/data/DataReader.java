package org.knime.core.data;

public interface DataReader<D extends Data> extends AutoCloseable {
	D read(long index);

	long size();
}
