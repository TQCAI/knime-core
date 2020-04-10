package org.knime.core.data;

public interface DataWriter<T> {

	void write(Data<T> data);

}
