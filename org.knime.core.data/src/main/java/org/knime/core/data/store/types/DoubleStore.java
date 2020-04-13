package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.store.DataStore;
import org.knime.core.data.store.StoreDataAccess;

public interface DoubleStore<T, V extends StoreDataAccess<T> & ReadableDoubleAccess & WritableDoubleAccess>
		extends DataStore<T, V> {

}
