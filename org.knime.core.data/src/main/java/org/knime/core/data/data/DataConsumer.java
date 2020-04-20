package org.knime.core.data.data;

public interface DataConsumer<D extends Data> {
	void accept(D data);
}
