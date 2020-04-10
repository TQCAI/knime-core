package org.knime.core.data.array.types;

public interface Array<A> extends AutoCloseable {
	boolean isMissing(int index);

	void setMissing(int index);

	A get();

	long size();
}
