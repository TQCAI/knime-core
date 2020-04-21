package org.knime.core.data.record;

import org.knime.core.data.store.DataReadStore;

public interface RecordReadStore extends DataReadStore<Record, RecordReaderConfig> {
	@Override
	RecordReader createReader(RecordReaderConfig config);
}
