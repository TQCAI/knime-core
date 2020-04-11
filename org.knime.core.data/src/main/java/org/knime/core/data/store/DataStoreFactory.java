package org.knime.core.data.store;

import org.knime.core.data.store.types.BooleanStore;
import org.knime.core.data.store.types.DoubleStore;
import org.knime.core.data.store.types.StringStore;

public interface DataStoreFactory {

	DoubleStore<?, ?> createDoubleStore();

	BooleanStore<?, ?> createBooleanStore();

	StringStore<?, ?> createStringStore();

}
