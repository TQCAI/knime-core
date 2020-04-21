package org.knime.core.data.store;

import org.knime.core.data.Data;

public interface DataStore<D extends Data> extends DataReadStore<D>, DataWriteStore<D> {
	// NB: Marker interface
}
