
package org.knime.core.data.store.cache;

import java.io.Flushable;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.knime.core.data.store.Data;
import org.knime.core.data.store.DataAccess;
import org.knime.core.data.store.DataCursor;
import org.knime.core.data.store.DataStore;

// TODO thread-safety...
// TODO sequential pre-loading etc
// TODO remember what has already been flushed (vs. what needs to be flushed)
// NB: Important: data must be flushed in order.
class CachedDataStore<T, V extends DataAccess<T>> implements DataStore<T, V>, Flushable {

	private final Map<Long, CachedData> m_indexCache = new TreeMap<>();
	private final Map<Data<T>, CachedData> m_dataCache = new HashMap<>();
	private final ReentrantReadWriteLock m_cacheLock = new ReentrantReadWriteLock(true);
	private final AtomicBoolean m_isClosedForAdding;
	private final DataStore<T, V> m_delegate;

	private long m_dataCounter;

	public CachedDataStore(final DataStore<T, V> delegate) {
		m_delegate = delegate;
		m_isClosedForAdding = new AtomicBoolean(false);
	}

	@Override
	public void flush() throws IOException {
		m_cacheLock.writeLock().lock();
		try {
			for (final Entry<Long, CachedData> entry : m_indexCache.entrySet()) {
				final CachedData cachedData = entry.getValue();
				if (!cachedData.isStored()) {
					m_delegate.addToStore(cachedData.get());
					cachedData.setStored();
				}

				// TODO keep data as long in memory as possible? How with off-heap? can't rely
				// on weak references etc.
				// we can release our internal reference.
				if (cachedData.decRefCounter()) {
					m_delegate.release(cachedData.get());
				}
			}
			// clear our cache. external references will still have access to data.
			m_dataCache.clear();
			m_indexCache.clear();

			// we can now close the delegate storing in case we were closed for adding
			// before as everything has been flushed.
			if (m_isClosedForAdding.get()) {
				m_delegate.closeForAdding();
			}
		} catch (Exception e) {
			throw new IOException(e);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public V createDataAccess() {
		return m_delegate.createDataAccess();
	}

	@Override
	public Data<T> create() {
		// we also block the creation of new data during flush etc.
		try {
			m_cacheLock.writeLock().lock();
			return m_delegate.create();
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public void release(final Data<T> data) {
		final CachedData cached = m_dataCache.get(data);
		if (cached == null) {
			// data not cached. we can just forward the release to delegate.
			m_delegate.release(data);
		} else if (cached.decRefCounter()) {
			// cache always has a reference on data until cleared.
			throw new IllegalStateException("Data released too manny times. Implementation error.");
		}
	}

	@Override
	public void addToStore(Data<T> data) {
		addToStore(data, m_dataCounter++, false);
	}

	private void addToStore(Data<T> data, long index, boolean isStored) {
		if (m_isClosedForAdding.get()) {
			throw new IllegalStateException("Data added after store has already been closed for adding.");
		}
		m_cacheLock.writeLock().lock();
		try {
			final CachedData cachedData = new CachedData(data, index, isStored);
			// Inc ref counter by one (=cache). The initial one is for whoever added the
			// data to the cache.
			cachedData.incRefCounter();
			m_indexCache.put(index, cachedData);
			m_dataCache.put(data, cachedData);
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public void close() throws Exception {
		for (final Entry<Data<T>, CachedData> data : m_dataCache.entrySet()) {
			if (!data.getValue().decRefCounter()) {
				// throw exception. We're killing the cache even if there are open
				// references to it... can only go wrong.
				throw new IllegalStateException(
						"Clearing all memory associated with store, even if there is still referenced data, which has not been released, yet!");
			} else {
				// release memory
				m_delegate.release(data.getKey());
			}
		}
		m_dataCache.clear();
		m_indexCache.clear();
	}

	@Override
	public void closeForAdding() {
		m_isClosedForAdding.set(true);
	}

	@Override
	public DataCursor<T> cursor() {
		return new DataCursor<T>() {

			private final DataCursor<T> m_delegateCursor = m_delegate.cursor();

			private long m_index = -1;

			@Override
			public Data<T> get() {
				// TODO pre-loading

				// TODO do we have to lock here while we're flushing?
				// Some thoughts: (1) we could block all reading nodes and avoid data
				// production on memory-alerts vs. (2) we could allow all accessing nodes to
				// read all in-memory chunks as we have them in memory anyways.
				m_cacheLock.readLock().lock();
				try {
					final CachedData entry = m_indexCache.get(m_index);
					final Data<T> data;
					if (entry == null) {
						addToStore(data = m_delegateCursor.get(), m_index, true);
					} else {
						data = entry.get();
						entry.incRefCounter();
						m_delegateCursor.fwd();
					}
					return data;
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

	class CachedData {
		private Data<T> m_data;
		private AtomicBoolean m_isStored;
		private AtomicInteger m_refCounter;
		private long m_index;

		CachedData(Data<T> data, long index, boolean isStored) {
			m_data = data;
			m_index = index;
			m_isStored = new AtomicBoolean(isStored);
			m_refCounter = new AtomicInteger(1);
		}

		void incRefCounter() {
			m_refCounter.getAndIncrement();
		}

		/**
		 * @return true if no more references exist
		 */
		boolean decRefCounter() {
			return m_refCounter.decrementAndGet() == 0;
		}

		boolean isStored() {
			return m_isStored.get();
		}

		void setStored() {
			m_isStored.set(true);
		}

		Data<T> get() {
			return m_data;
		}

		long getIndex() {
			return m_index;
		}
	}
}
