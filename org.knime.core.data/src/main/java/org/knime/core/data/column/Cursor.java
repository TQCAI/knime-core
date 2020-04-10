package org.knime.core.data.column;

public interface Cursor<T> extends AutoCloseable {

	T get();

	void fwd();

	boolean canFwd();
}
