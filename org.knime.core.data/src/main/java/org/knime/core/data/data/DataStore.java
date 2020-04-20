package org.knime.core.data.data;

import org.knime.core.data.api.NativeType;

//TODO Split interface into Read/Write
public interface DataStore extends AutoCloseable {

	NativeType<?, ?>[] getColumnTypes();

}
