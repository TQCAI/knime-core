package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableStringAccess;
import org.knime.core.data.api.access.WritableStringAccess;
import org.knime.core.data.store.StoreDataAccess;
import org.knime.core.data.store.DataStore;

public interface StringStore<T, V extends StoreDataAccess<T> & ReadableStringAccess & WritableStringAccess>
		extends DataStore<T, V> {

}
