
package org.knime.core.data.chunked;

import java.io.Flushable;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;
import org.knime.core.data.chunked.CachedChunkedDataStore.CachedData;

// TODO thread-safety...
// TODO sequential pre-loading etc
// TODO there must be smarter sequential caches out there
// Important: partitions must be flushed in order.
public final class CachedChunkedDataStore<T> implements Flushable, ChunkedDataStore<T, CachedData<T>, DataAccess<T>> {

	private final Map<Long, CachedData<T>> m_cache = new TreeMap<>();
	private final ReentrantReadWriteLock m_cacheLock = new ReentrantReadWriteLock(true);
	private final ChunkedDataStore<T, Data<T>, DataAccess<T>> m_delegate;

	private long m_dataCounter;

	public CachedChunkedDataStore(final ChunkedDataStore<T, Data<T>, DataAccess<T>> delegate) {
		m_delegate = delegate;
	}

	// TODO Memory alert or similar: Block adding new stuff to cache.
	@Override
	public void flush() throws IOException {
		m_cacheLock.writeLock().lock();
		try {
			for (final Entry<Long, CachedData<T>> entry : m_cache.entrySet()) {
				final CachedData<T> data = entry.getValue();
				m_delegate.addData(data);
				entry.getValue().close();
			}
			m_cache.clear();
		} catch (Exception e) {
			// TODO acceptable?
			throw new IOException(e);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public DataAccess<T> createDataAccess() {
		return m_delegate.createDataAccess();
	}

	@Override
	public CachedData<T> createData() {
		// We have to intercept the 'close()' of data to make sure that it's not
		// closed after it has been added back to cache.
		final CachedData<T> referenced = new CachedData<T>(m_delegate.createData());
		referenced.incRefCtr();
		return referenced;
	}

	@Override
	public ChunkedDataCursor<T, CachedData<T>> cursor() {
		return new ChunkedDataCursor<T, CachedData<T>>() {

			private final ChunkedDataCursor<T, Data<T>> m_delegateCursor = m_delegate.cursor();

			private long m_index = -1;

			@Override
			public CachedData<T> get() {
				// TODO pre-loading

				m_cacheLock.readLock().lock();
				try {
					CachedData<T> entry = m_cache.get(m_index);
					if (entry == null) {
						entry = new CachedData<T>(m_delegateCursor.get());
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
		};
	}

	@Override
	public void addData(CachedData<T> data) {
		m_cacheLock.writeLock().lock();
		try {
			// TODO async pre-flushing
			m_cache.put(m_dataCounter++, data);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	static class CachedData<T> implements Data<T> {

		private final AtomicInteger m_refs;
		private final Data<T> m_delegate;

		public CachedData(Data<T> delegate) {
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
