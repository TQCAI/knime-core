
package org.knime.core.data.api.column;

public interface ColumnType {
	PrimitiveType[] getPrimitiveTypes();

	String name();
}
