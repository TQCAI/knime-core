package org.knime.core.data.store;

import org.knime.core.data.store.types.BooleanStore;
import org.knime.core.data.store.types.DoubleAccess;
import org.knime.core.data.store.types.DoubleStore;
import org.knime.core.data.store.types.StringStore;

public interface DataStoreFactory extends AutoCloseable {

	BooleanStore<?, ?> createBooleanStore();

	<T, A extends DoubleAccess<T>> DoubleStore<T, A> createDoubleStore();

	StringStore<?, ?> createStringStore();

}
