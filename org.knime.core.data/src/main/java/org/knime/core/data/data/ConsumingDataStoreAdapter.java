package org.knime.core.data.data;

public interface ConsumingDataStoreAdapter {

	boolean hasAdapter(long colIndex);

	<D extends Data> DataConsumer<D> createAdapter(long colIndex);

}
