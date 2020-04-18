package org.knime.core.data.array;

public interface ArrayWriteStore<A extends Array> {

	void add(A array);

	void closeWriteStore();
}
