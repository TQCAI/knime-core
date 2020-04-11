package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableStringAccess;
import org.knime.core.data.api.access.WritableStringAccess;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataStore;

public interface StringDataStore<T, V extends DataAccess<T> & ReadableStringAccess & WritableStringAccess>
		extends DataStore<T, V> {

}
