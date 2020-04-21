package org.knime.core.data;

public interface Cursor<A> extends AutoCloseable {

	A get();

	void fwd();

	boolean canFwd();
}
