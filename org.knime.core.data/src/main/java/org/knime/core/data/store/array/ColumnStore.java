package org.knime.core.data.store.array;

public interface ColumnStore<A extends Array> {

	void add(A array);

	A get(long index);

	void closeForAdditions();

	long size();

}
