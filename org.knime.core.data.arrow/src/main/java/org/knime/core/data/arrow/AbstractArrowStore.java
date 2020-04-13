package org.knime.core.data.arrow;

import java.io.File;
import java.io.IOException;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.store.Data;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataCursor;
import org.knime.core.data.store.DataStore;

abstract class AbstractArrowStore<T extends FieldVector, V extends DataAccess<T>> implements DataStore<T, V> {

	private final long m_maxCapacity;
	private final BufferAllocator m_allocator;

	private final File m_file;
	private final FieldVectorWriter<T> m_writer;

	// at some point, when we instantiate readers without prior writing, this field
	// needs to come from somewhere else.
	private long m_storedData;

	AbstractArrowStore(final BufferAllocator allocator, File file, final long chunkSize) {
		m_maxCapacity = chunkSize;
		m_allocator = allocator;
		m_file = file;
		try {
			m_writer = new FieldVectorWriter<>(m_file);
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public void close() throws Exception {
		// close all memory etc. keep written files.
		m_allocator.close();
	}

	@Override
	public void closeForWriting() {
		try {
			// close writer.
			m_writer.close();
		} catch (Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public void add(final Data<T> data) {
		try {
			m_writer.flush(data.get());
			m_storedData++;
		} catch (IOException e) {
			// TODO
			throw new RuntimeException(e);
		}
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
		return new FieldVectorData<>(vector, m_maxCapacity);
	}

	// TODO: Cursor is independent of Arrow
	@Override
	public DataCursor<T> cursor() {
		return new DataCursor<T>() {

			final FieldVectorReader<T> m_reader;
			{
				try {
					m_reader = new FieldVectorReader<>(m_file,
							m_allocator.newChildAllocator("Reader", 0, m_allocator.getLimit()));
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}

			private long m_index = -1;

			@Override
			public Data<T> get() {
				try {
					return new FieldVectorData<>(m_reader.load(m_index), m_maxCapacity);
				} catch (IOException e) {
					// TODO
					throw new RuntimeException(e);
				}
			}

			@Override
			public void fwd() {
				m_index++;
			}

			@Override
			public boolean canFwd() {
				return m_index < m_storedData - 1;
			}

			@Override
			public void close() throws Exception {
				m_reader.close();
			}
		};
	}

	protected abstract T create(BufferAllocator allocator, long capacity);
}
