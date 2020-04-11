package org.knime.core.data.array;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataStore;
import org.knime.core.data.DataAccess;
import org.knime.core.data.DataCursor;
import org.knime.core.data.chunk.WritableData;
import org.knime.core.data.column.Domain;

abstract class AbstractArrayColumnStore<T extends Array<?>, V extends DataAccess<T>> implements DataStore<T, V> {

	private final long m_maxCapacity;
	private final List<ReadableArrayData<T>> m_list = new ArrayList<>();
	private Domain m_domain;

	AbstractArrayColumnStore(final long chunkSize) {
		m_maxCapacity = chunkSize;
	}

	@Override
	public void close() throws Exception {
		m_list.clear();
	}

	@Override
	public Domain getDomain() {
		if (m_domain == null) {
			m_domain = initDomain();
		}
		return m_domain;
	}

	@Override
	public WritableData<T> create() {
		return new WritableArrayData<>(create(m_maxCapacity));
	}

	// TODO: Cursor is independent of array-based implementation
	@Override
	public DataCursor<T> cursor() {
		return new DataCursor<T>() {

			private long m_index = -1;

			@Override
			public ReadableArrayData<T> get() {
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

			@Override
			public void move(final long steps) {
				m_index += steps;
			}
		};
	}

	protected abstract T create(long capacity);

	protected abstract Domain initDomain();
}
