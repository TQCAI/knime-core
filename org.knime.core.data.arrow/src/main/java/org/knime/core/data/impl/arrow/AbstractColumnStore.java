package org.knime.core.data.impl.arrow;

import java.util.List;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.ColumnStore;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;
import org.knime.core.data.column.Domain;

abstract class AbstractColumnStore<T extends FieldVector, V extends DataChunkAccess<T>>
		implements ColumnStore<T, FieldVectorDataChunk<T>, V> {

	private final long m_maxCapacity;
	private final BufferAllocator m_allocator;

	private List<FieldVectorDataChunk<T>> m_list;
	private Domain m_domain;

	AbstractColumnStore(final BufferAllocator allocator, final long chunkSize) {
		m_maxCapacity = chunkSize;
		m_allocator = allocator;
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
	public void addData(final FieldVectorDataChunk<T> data) {
		m_list.add(data);
	}

	@Override
	public FieldVectorDataChunk<T> createData() {
		final T vector = create(m_allocator, m_maxCapacity);
		// TODO for some reason vector has two references on it at that point. We'll
		// manually remove one to be consistent with our framework. We may want to open
		// a bug report @ Arrow. Likely, we're doing something wrong on our side in the
		// way we create vectors.
		ArrowUtils.releaseVector(vector);
		return new FieldVectorDataChunk<>(vector);
	}

	// TODO: Cursor is independent of Arrow
	@Override
	public DataChunkCursor<T, FieldVectorDataChunk<T>> cursor() {
		return new DataChunkCursor<T, FieldVectorDataChunk<T>>() {

			private long m_index = -1;

			@Override
			public FieldVectorDataChunk<T> get() {
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
				// TODO as soon as we have a reader though...
			}

			@Override
			public void move(final long steps) {
				m_index += steps;
			}
		};
	}

	protected abstract T create(BufferAllocator allocator, long capacity);

	protected abstract Domain initDomain();
}
