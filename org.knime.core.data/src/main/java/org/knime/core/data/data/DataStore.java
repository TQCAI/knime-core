package org.knime.core.data.data;

public interface DataStore extends AutoCloseable {

	<D extends Data> DataConsumer<D> getConsumer(long columnIndex);

	<D extends Data> DataLoader<D> getLoader(long columnIndex);

}
