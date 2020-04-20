package org.knime.core.data.data;

import org.knime.core.data.api.PrimitiveType;

//TODO Split interface into Read/Write
public interface DataStore extends AutoCloseable {

	<D extends Data> DataConsumer<D> getConsumer(long columnIndex);

	<D extends Data> DataLoader<D> createLoader(long columnIndex);

	PrimitiveType[] getPrimitiveSpec();

}
