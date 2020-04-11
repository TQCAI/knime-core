package org.knime.core.data;

public interface DataReader<T> extends AutoCloseable {

	Data<T> read(long index);

}
