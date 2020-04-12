package org.knime.core.data.array;

public interface Array<A> {
	boolean isMissing(long index);

	void setMissing(long index);

	A get();

	long size();

	void release();
}
