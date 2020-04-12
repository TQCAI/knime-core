
package org.knime.core.data.store.cache;

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
// TODO there must be smarter sequential caches out there
// NB: Important: data must be flushed in order.
// TODO remember what has already been flushed (vs. what needs to be flushed)
class CachedDataStore<T, V extends DataAccess<T>> implements DataStore<T, V> {

	private final Map<Long, CachedData> m_indexCache = new TreeMap<>();

	private final Map<Data<T>, CachedData> m_dataCache = new HashMap<>();

	private final ReentrantReadWriteLock m_cacheLock = new ReentrantReadWriteLock(true);

	private long m_dataCounter;

	private DataStore<T, V> m_delegate;

	public CachedDataStore(final DataStore<T, V> delegate) {
		m_delegate = delegate;
	}

	// TODO Memory alert or similar: Block adding new stuff to cache.
	// Also clears cache.
	public void flush() throws IOException {
		m_cacheLock.writeLock().lock();
		try {
			// TODO strong assumption on order.
			for (final Entry<Long, CachedData> entry : m_indexCache.entrySet()) {
				final CachedData data = entry.getValue();
				if (!data.store()) {
					m_delegate.store(data.get());
				}
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
	public V createDataAccess() {
		return m_delegate.createDataAccess();
	}

	@Override
	public Data<T> create() {
		return m_delegate.create();
	}

	@Override
	public void release(Data<T> data) {
		// Data could have been requested several times.
		// Check ref-counter on data. in case 0, release also from delegate.
		final CachedData cached = m_dataCache.get(data);
		if (cached.decRefCounter()) {
			m_dataCache.remove(data);
			m_indexCache.remove(cached.getIndex());
			m_delegate.release(data);
		}
	}

	@Override
	public void store(Data<T> data) {
		m_cacheLock.writeLock().lock();
		try {
			// TODO increment ref counter on data
			final CachedData cachedData = new CachedData(data, m_dataCounter);
			// Inc ref counter by one (=cache).
			cachedData.incRefCounter();
			m_indexCache.put(m_dataCounter, cachedData);
			m_dataCache.put(data, cachedData);
			m_dataCounter++;
		} finally {
			m_cacheLock.writeLock().unlock();
		}
	}

	@Override
	public void close() throws Exception {
		clear();
	}

	@Override
	public DataCursor<T> cursor() {
		return new DataCursor<T>() {

			private final DataCursor<T> m_delegateCursor = m_delegate.cursor();

			private long m_index = -1;

			@Override
			public Data<T> get() {
				// TODO pre-loading

				m_cacheLock.readLock().lock();
				try {
					final CachedData entry = m_indexCache.get(m_index);
					final Data<T> data;
					if (entry == null) {
						store(data = m_delegateCursor.get());
					} else {
						data = entry.get();
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

	private void clear() throws Exception {
		for (final Data<T> data : m_dataCache.keySet()) {
			// release our own reference!
			release(data);
		}
		// this one we really have to clear now
		m_indexCache.clear();
	}

	class CachedData {
		private Data<T> m_data;
		private AtomicBoolean m_isStored;
		private AtomicInteger m_refCounter;
		private long m_index;

		CachedData(Data<T> data, long index) {
			m_data = data;
			m_index = index;
			m_isStored = new AtomicBoolean(false);
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

		boolean store() {
			return m_isStored.getAndSet(true);
		}

		Data<T> get() {
			return m_data;
		}

		long getIndex() {
			return m_index;
		}
	}

	@Override
	public void closeForWriting() {
		m_delegate.closeForWriting();
	}

}
