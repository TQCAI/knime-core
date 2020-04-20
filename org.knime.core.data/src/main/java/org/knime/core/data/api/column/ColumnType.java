
package org.knime.core.data.api.column;

import org.knime.core.data.api.PrimitiveType;

public interface ColumnType {
	PrimitiveType[] getPrimitiveTypes();

	String name();
}
