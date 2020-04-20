package org.knime.core.data.api.row;

public interface WritableRowTable {
	long getNumColumns();

	WritableRow getWritableRow();

}
