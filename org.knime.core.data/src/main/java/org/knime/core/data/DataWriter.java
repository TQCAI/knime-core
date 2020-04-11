package org.knime.core.data;

public interface DataWriter<T> extends AutoCloseable {

	void write(Data<T> data);

}
