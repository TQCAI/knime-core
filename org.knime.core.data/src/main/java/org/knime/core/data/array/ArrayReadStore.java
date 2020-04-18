package org.knime.core.data.array;

public interface ArrayReadStore<A extends Array> {
	A get(long index);

	long size();

	void closeReadStore();
}
