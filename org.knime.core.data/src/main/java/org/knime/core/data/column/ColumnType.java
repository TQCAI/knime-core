
package org.knime.core.data.column;

public interface ColumnType {
	NativeColumnType[] getNativeTypes();

	String name();
}
