package org.knime.core.data.array;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.ColumnStore;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;
import org.knime.core.data.column.Domain;

abstract class AbstractArrayColumnStore<T extends Array<?>, V extends DataChunkAccess<T>>
		implements ColumnStore<T, ArrayDataChunk<T>, V> {

	private final long m_maxCapacity;
	private final List<ArrayDataChunk<T>> m_list = new ArrayList<>();
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
	public void addData(final ArrayDataChunk<T> data) {
		// TODO write to disc
		m_list.add(data);
	}

	@Override
	public ArrayDataChunk<T> createData() {
		return new ArrayDataChunk<>(create(m_maxCapacity));
	}

	// TODO: Cursor is independent of array-based implementation
	@Override
	public DataChunkCursor<T, ArrayDataChunk<T>> cursor() {
		return new DataChunkCursor<T, ArrayDataChunk<T>>() {

			private long m_index = -1;

			@Override
			public ArrayDataChunk<T> get() {
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
