
package org.knime.core.data.array;

import org.knime.core.data.store.DataStoreFactory;
import org.knime.core.data.store.types.BooleanDataStore;
import org.knime.core.data.store.types.DoubleDataStore;
import org.knime.core.data.store.types.StringDataStore;

public class ArrayColumnStoreFactory implements DataStoreFactory {

	private final long m_chunkSize;

	public ArrayColumnStoreFactory(final long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@Override
	public DoubleDataStore<?, ?> createDoubleStore() {
		return new DoubleArrayStore(m_chunkSize);
	}

	@Override
	public BooleanDataStore<?, ?> createBooleanStore() {
		return new BooleanArrayStore(m_chunkSize);
	}

	@Override
	public StringDataStore<?, ?> createStringStore() {
		return new StringArrayStore(m_chunkSize);
	}

}
