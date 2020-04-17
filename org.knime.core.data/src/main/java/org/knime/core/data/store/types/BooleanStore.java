package org.knime.core.data.store.types;

import org.knime.core.data.store.DataStore;

public interface BooleanStore<T, A extends BooleanAccess<T>> extends DataStore<T, A> {
	// NB: Marker interface
}
