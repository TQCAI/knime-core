
package org.knime.core.data.api.column;

public interface ColumnType {
	NativeColumnType[] getNativeTypes();

	String name();
}
