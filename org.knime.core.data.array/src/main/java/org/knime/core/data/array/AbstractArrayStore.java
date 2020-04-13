package org.knime.core.data.array;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.store.Data;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataCursor;
import org.knime.core.data.store.DataStore;

abstract class AbstractArrayStore<T extends Array<?>, V extends DataAccess<T>> implements DataStore<T, V> {

	private final long m_maxCapacity;
	private final List<Data<T>> m_list = new ArrayList<>();

	AbstractArrayStore(final long chunkSize) {
		m_maxCapacity = chunkSize;
	}

	@Override
	public void close() throws Exception {
		m_list.clear();
	}

	@Override
	public Data<T> create() {
		return new ArrayData<>(create(m_maxCapacity));
	}

	@Override
	public void add(Data<T> data) {
		// TODO of course flush is more appropriate here
		m_list.add(data);
	}

	@Override
	public void release(Data<T> data) {
		// Nothing to do here
		data.get().release();
	}

	@Override
	public void closeForWriting() {
		// TODO
	}

	@Override
	public DataCursor<T> cursor() {
		return new DataCursor<T>() {

			private long m_index = -1;

			@Override
			public Data<T> get() {
				// TODO load from disc if required!
				return m_list.get((int) m_index);
			}

			@Override
			public void fwd() {
				m_index++;
			}

			@Override
			public boolean canFwd() {
				return m_index < m_list.size() - 1;
			}

			@Override
			public void close() throws Exception {
				// nothing to do yet.
			}
		};
	}

	protected abstract T create(long capacity);

}
