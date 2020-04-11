
package org.knime.core.data.array;

import org.knime.core.data.store.DataStoreFactory;
import org.knime.core.data.store.types.BooleanStore;
import org.knime.core.data.store.types.DoubleStore;
import org.knime.core.data.store.types.StringStore;

public class ArrayColumnStoreFactory implements DataStoreFactory {

	private final long m_chunkSize;

	public ArrayColumnStoreFactory(final long chunkSize) {
		m_chunkSize = chunkSize;
	}

	@Override
	public DoubleStore<?, ?> createDoubleStore() {
		return new DoubleArrayStore(m_chunkSize);
	}

	@Override
	public BooleanStore<?, ?> createBooleanStore() {
		return new BooleanArrayStore(m_chunkSize);
	}

	@Override
	public StringStore<?, ?> createStringStore() {
		return new StringArrayStore(m_chunkSize);
	}

}
