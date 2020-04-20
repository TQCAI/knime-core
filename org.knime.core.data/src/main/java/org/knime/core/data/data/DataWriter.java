package org.knime.core.data.data;

public interface DataWriter<A extends Data> extends AutoCloseable {
	void write(A array);
}
