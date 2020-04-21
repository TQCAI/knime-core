package org.knime.core.data.store;

import org.knime.core.data.Data;

public interface DataStore<D extends Data, C extends DataReaderConfig>
		extends DataReadStore<D, C>, DataWriteStore<D>, AutoCloseable {
	// NB: Marker interface
}
