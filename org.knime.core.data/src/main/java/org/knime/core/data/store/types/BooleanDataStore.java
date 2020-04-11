package org.knime.core.data.store.types;

import org.knime.core.data.api.access.ReadableBooleanAccess;
import org.knime.core.data.api.access.WritableBooleanAccess;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataStore;

public interface BooleanDataStore<T, V extends DataAccess<T> & ReadableBooleanAccess & WritableBooleanAccess>
		extends DataStore<T, V> {

}
