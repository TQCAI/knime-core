package org.knime.core.data.store.types;

import org.knime.core.data.store.DataStore;

public interface StringStore<T, A extends StringAccess<T>> extends DataStore<T, A> {
// NB: Marker interface
}
