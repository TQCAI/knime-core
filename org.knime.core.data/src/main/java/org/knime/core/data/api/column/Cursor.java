package org.knime.core.data.api.column;

public interface Cursor<A> extends AutoCloseable {

	A get();

	void fwd();

	boolean canFwd();
}
