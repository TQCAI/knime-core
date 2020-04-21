package org.knime.core.data.data;

public interface DataLoader<D> extends AutoCloseable {
	D load(long index);

	long size();
}
