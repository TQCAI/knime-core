package org.knime.core.data.api.column;

public interface Cursor<T> extends AutoCloseable {

	T get();

	void fwd();

	boolean canFwd();
}
