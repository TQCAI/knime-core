package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableBooleanAccess;
import org.knime.core.data.api.access.WritableBooleanAccess;
import org.knime.core.data.store.StoreDataAccess;
import org.knime.core.data.store.DataStore;

public interface BooleanStore<T, V extends StoreDataAccess<T> & ReadableBooleanAccess & WritableBooleanAccess>
		extends DataStore<T, V> {

}
