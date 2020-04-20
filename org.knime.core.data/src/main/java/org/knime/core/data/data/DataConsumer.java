package org.knime.core.data.data;

public interface DataConsumer<D extends Data> extends AutoCloseable {
	void accept(long index, D data);
}
