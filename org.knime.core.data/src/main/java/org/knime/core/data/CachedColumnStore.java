
package org.knime.core.data;

import java.io.Flushable;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.knime.core.data.chunk.DataChunk;
import org.knime.core.data.chunk.DataChunkAccess;
import org.knime.core.data.chunk.DataChunkCursor;

// TODO thread-safety...
// TODO sequential pre-loading etc
// TODO there must be smarter sequential caches out there
// Important: partitions must be flushed in order.
class CachedColumnStore<T> implements Flushable, ColumnStore<T, DataChunkAccess<T>> {

	private final Map<Long, CachedDataChunk<T>> m_cache = new TreeMap<>();
	private final ReentrantReadWriteLock m_cacheLock = new ReentrantReadWriteLock(true);
	private final ColumnStore<T, DataChunkAccess<T>> m_delegate;

	private long m_dataCounter;

	public CachedColumnStore(final ColumnStore<T, DataChunkAccess<T>> delegate) {
		m_delegate = delegate;
	}

	// TODO Memory alert or similar: Block adding new stuff to cache.
	// Also clears cache.
	@Override
	public void flush() throws IOException {
		m_cacheLock.writeLock().lock();
		try {
			for (final Entry<Long, CachedDataChunk<T>> entry : m_cache.entrySet()) {
				final CachedDataChunk<T> data = entry.getValue();
				m_delegate.addData(data);
			}
			clear();
		} catch (Exception e) {
			// TODO acceptable?
			throw new IOException(e);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public DataChunkAccess<T> createDataAccess() {
		return m_delegate.createDataAccess();
	}

	@Override
	public CachedDataChunk<T> createData() {
		// We have to intercept the 'close()' of data to make sure that it's not
		// closed after it has been added back to cache.
		final CachedDataChunk<T> referenced = new CachedDataChunk<T>(m_delegate.createData());
		referenced.incRefCtr();
		return referenced;
	}

	@Override
	public DataChunkCursor<T> cursor() {
		return new DataChunkCursor<T>() {

			private final DataChunkCursor<T> m_delegateCursor = m_delegate.cursor();

			private long m_index = -1;

			@Override
			public CachedDataChunk<T> get() {
				// TODO pre-loading

				m_cacheLock.readLock().lock();
				try {
					CachedDataChunk<T> entry = m_cache.get(m_index);
					if (entry == null) {
						entry = new CachedDataChunk<T>(m_delegateCursor.get());
						m_cache.put(m_index, entry);
						// TODO thread-safety?
						// retain for external
						// TODO: Acquire write lock, update, etc.
					}
					entry.incRefCtr();
					return entry;
				} finally {
					m_cacheLock.readLock().unlock();
				}
			}

			@Override
			public void fwd() {
				m_index++;
			}

			@Override
			public boolean canFwd() {
				// we can't delegate this. We might have cached more than we've actually
				// written.
				return m_index < m_dataCounter - 1;
			}

			@Override
			public void close() throws Exception {
				m_delegateCursor.close();
			}

			@Override
			public void move(long steps) {
				m_index += steps;
			}
		};
	}

	private void clear() throws Exception {
		for (final CachedDataChunk<T> entry : m_cache.values()) {
			entry.close();
		}
		m_cache.clear();
	}

	@Override
	public void addData(DataChunk<T> data) {
		m_cacheLock.writeLock().lock();
		try {
			// TODO async pre-flushing
			final CachedDataChunk<T> chunk;
			if (data instanceof CachedDataChunk) {
				chunk = (CachedDataChunk<T>) data;
			} else {
				chunk = new CachedDataChunk<T>(data);
			}
			m_cache.put(m_dataCounter++, chunk);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public void close() throws Exception {
		clear();
		m_delegate.close();
	}

	static class CachedDataChunk<T> implements DataChunk<T> {

		private final AtomicInteger m_refs;
		private final DataChunk<T> m_delegate;

		public CachedDataChunk(DataChunk<T> delegate) {
			m_delegate = delegate;
			m_refs = new AtomicInteger(1);
		}

		void incRefCtr() {
			m_refs.incrementAndGet();
		}

		@Override
		public T get() {
			return m_delegate.get();
		}

		@Override
		public void close() throws Exception {
			// TODO distinguish between read / write case. We can't read before write.
			if (m_refs.decrementAndGet() == 0) {
				m_delegate.close();
			}
		}

		@Override
		public long getCapacity() {
			return m_delegate.getCapacity();
		}

		@Override
		public void setValueCount(long numValues) {
			m_delegate.setValueCount(numValues);
		}

		@Override
		public long getValueCount() {
			return m_delegate.getValueCount();
		}
	}

}
