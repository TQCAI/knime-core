package org.knime.core.data.impl.arrow;

import java.util.ArrayList;
import java.util.List;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.store.Data;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataCursor;
import org.knime.core.data.store.DataStore;

abstract class AbstractArrowStore<T extends FieldVector, V extends DataAccess<T>> implements DataStore<T, V> {

	private final long m_maxCapacity;
	private final BufferAllocator m_allocator;

	private final List<Data<T>> m_list = new ArrayList<>();

	AbstractArrowStore(final BufferAllocator allocator, final long chunkSize) {
		m_maxCapacity = chunkSize;
		m_allocator = allocator;
	}

	@Override
	public void close() throws Exception {
		// Close means: clear memory
		for (final Data<T> data : m_list) {
			release(data);
		}
		m_list.clear();
	}

	@Override
	public void store(final Data<T> data) {
		m_list.add(data);
	}

	@Override
	public void release(Data<T> data) {
		data.get().close();
	}

	@Override
	public Data<T> create() {
		final T vector = create(m_allocator, m_maxCapacity);
		// TODO for some reason vector has two references on it at that point. We'll
		// manually remove one to be consistent with our framework. We may want to open
		// a bug report @ Arrow. Likely, we're doing something wrong on our side in the
		// way we create vectors.
		ArrowUtils.releaseVector(vector);
		return new FieldVectorData<>(vector);
	}

	// TODO: Cursor is independent of Arrow
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
				// TODO as soon as we have a reader though...
			}
		};
	}

	protected abstract T create(BufferAllocator allocator, long capacity);
}
