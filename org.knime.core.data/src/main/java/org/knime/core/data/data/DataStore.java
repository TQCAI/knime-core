package org.knime.core.data.data;

import org.knime.core.data.api.PrimitiveType;

//TODO Split interface into Read/Write
public interface DataStore extends AutoCloseable {

	PrimitiveType<?, ?>[] getPrimitiveSpec();

}
