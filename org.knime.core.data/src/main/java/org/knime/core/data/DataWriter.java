package org.knime.core.data;

public interface DataWriter<D> {
	void write(D data);
}
