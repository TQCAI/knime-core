package org.knime.core.data.record;

import org.knime.core.data.store.DataReaderConfig;

public interface RecordReaderConfig extends DataReaderConfig {
	int[] getSelectedColumnIndices();

	// TODO RowRange Object instead of long[]?
	long[] getRowRange();
}
