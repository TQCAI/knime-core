package org.knime.core.data.record;

import org.knime.core.data.store.DataWriteStore;

public interface RecordWriteStore extends DataWriteStore<Record> {
	RecordWriter getWriter();
}
