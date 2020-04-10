package org.knime.core.data;

public interface WritableRowTable {
	long getNumColumns();

	WritableRow getWritableRow();
}
