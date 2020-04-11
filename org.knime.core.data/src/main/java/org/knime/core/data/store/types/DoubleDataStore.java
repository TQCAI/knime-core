package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataStore;

public interface DoubleDataStore<T, V extends DataAccess<T> & ReadableDoubleAccess & WritableDoubleAccess>
		extends DataStore<T, V> {

}
