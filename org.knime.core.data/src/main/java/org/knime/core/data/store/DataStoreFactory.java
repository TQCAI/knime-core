package org.knime.core.data.store;

import org.knime.core.data.store.types.BooleanDataStore;
import org.knime.core.data.store.types.DoubleDataStore;
import org.knime.core.data.store.types.StringDataStore;

public interface DataStoreFactory {

	DoubleDataStore<?, ?> createDoubleStore();

	BooleanDataStore<?, ?> createBooleanStore();

	StringDataStore<?, ?> createStringStore();

}
