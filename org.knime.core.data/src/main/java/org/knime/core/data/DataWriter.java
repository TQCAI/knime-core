package org.knime.core.data;

public interface DataWriter<D> extends AutoCloseable {
	void write(D data);
}
