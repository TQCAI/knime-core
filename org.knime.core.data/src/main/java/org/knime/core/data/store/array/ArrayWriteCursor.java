package org.knime.core.data.store.array;

import java.util.function.Supplier;

public class ArrayWriteCursor<A extends Array, T extends ArrayAccess<A>> implements ArrayCursor<T> {

	private final ColumnStore<A> m_store;
	private final Supplier<A> m_factory;

	private A m_currentArray;
	private T m_access;

	private long m_currentDataMaxIndex;
	private long m_index;

	public ArrayWriteCursor(final Supplier<A> factory, final ColumnStore<A> store, final T access) {
		switchToNextArray();
		m_factory = factory;
		m_store = store;
		m_access = access;
	}

	private void switchToNextArray() {
		try {
			releaseCurrentData();
			m_currentArray = m_factory.get();
			m_access.updateStorage(m_currentArray);
			m_currentDataMaxIndex = m_currentArray.getMaxCapacity() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	private void releaseCurrentData() {
		m_currentArray.setNumValues(m_index);
		m_store.add(m_currentArray);
		m_currentArray.release();
	}

	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextArray();
			m_index = 0;
		}
		m_access.fwd();
	}

	public T get() {
		return m_access;
	}

	@Override
	public void close() throws Exception {
		releaseCurrentData();
		m_store.closeForAdditions();
	}

	@Override
	public boolean canFwd() {
		return true;
	}
}