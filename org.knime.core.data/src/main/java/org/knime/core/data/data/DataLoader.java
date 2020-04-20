package org.knime.core.data.data;

public interface DataLoader<D extends Data> extends AutoCloseable {
	D load(long index);

	long size();
}
