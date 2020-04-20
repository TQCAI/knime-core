package org.knime.core.data.data;

public interface ConsumingDataStore extends DataStore {
	<D extends Data> DataConsumer<D> getConsumer(long columnIndex);
}
