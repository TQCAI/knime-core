package org.knime.core.data.data;

public interface ConsumingDataStoreAdapter {

	boolean hasAdapter(long index);

	<D extends Data> DataConsumer<D> createAdapter(long index);

}
