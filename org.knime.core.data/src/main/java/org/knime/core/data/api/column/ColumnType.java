
package org.knime.core.data.api.column;

import org.knime.core.data.api.NativeType;

public interface ColumnType {
	NativeType<?, ?>[] getPrimitiveTypes();

	String name();
}
