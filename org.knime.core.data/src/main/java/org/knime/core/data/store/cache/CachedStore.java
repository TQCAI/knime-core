//
//package org.knime.core.data.store.cache;
//
//import java.io.Flushable;
//import java.io.IOException;
//import java.util.Map;
//import java.util.Map.Entry;
//import java.util.TreeMap;
//import java.util.concurrent.atomic.AtomicBoolean;
//import java.util.concurrent.atomic.AtomicInteger;
//import java.util.concurrent.locks.ReentrantReadWriteLock;
//
//import org.knime.core.data.store.Data;
//import org.knime.core.data.store.WritableDataStore;
//
//// TODO thread-safety...
//// TODO sequential pre-loading etc
//// TODO remember what has already been flushed (vs. what needs to be flushed)
//// TODO: DataStore should be split into ReadableDataStore and WritableDataStore
//// NB: Important: data must be flushed in order.
//class CachedStore<T> implements WritableDataStore<T>, Flushable {
//
//	private final Map<Long, CachedData> m_indexCache = new TreeMap<>();
//	private final ReentrantReadWriteLock m_cacheLock = new ReentrantReadWriteLock(true);
//
//	private WritableDataStore<T> m_delegate;
//	private long m_numData;
//
//	public CachedStore(final WritableDataStore<T> delegate) {
//		m_delegate = delegate;
//	}
//
//	@Override
//	public void flush() throws IOException {
//		m_cacheLock.writeLock().lock();
//		try {
//			for (final Entry<Long, CachedData> entry : m_indexCache.entrySet()) {
//				final CachedData cachedData = entry.getValue();
//				if (!cachedData.isStored()) {
//					m_delegate.add(cachedData.get());
//					cachedData.setStored();
//				}
//			}
//			// clear our cache. external references will still have access to data.
//			m_indexCache.clear();
//
//		} catch (Exception e) {
//			throw new IOException(e);
//		} finally {
//			m_cacheLock.writeLock().unlock();
//		}
//	}
//
//	@Override
//	public void add(Data<T> data) {
//		data.retain();
//		m_cacheLock.writeLock().lock();
//		try {
//			m_numData++;
//			final CachedData cachedData = new CachedData(data, m_n, isStored);
//			// Inc ref counter by one (=cache). The initial one is for whoever added the
//			// data to the cache.
//			cachedData.incRefCounter();
//			m_indexCache.put(m_numData, cachedData);
//		} finally {
//			m_cacheLock.writeLock().unlock();
//		}
//	}
//
//	@Override
//	public Data<T> get(long index) {
//		return m_indexCache.get(index).get();
//	}
//
//	@Override
//	public long getNumStored() {
//		return m_numData;
//	}
//
//	@Override
//	public void close() throws Exception {
//		m_indexCache.clear();
//		m_delegate.close();
//	}
//
//	class CachedData {
//		private Data<T> m_data;
//		private AtomicBoolean m_isStored;
//		private AtomicInteger m_refCounter;
//		private long m_index;
//
//		CachedData(Data<T> data, long index, boolean isStored) {
//			m_data = data;
//			m_index = index;
//			m_isStored = new AtomicBoolean(isStored);
//			m_refCounter = new AtomicInteger(1);
//		}
//
//		void incRefCounter() {
//			m_refCounter.getAndIncrement();
//		}
//
//		/**
//		 * @return true if no more references exist
//		 */
//		boolean decRefCounter() {
//			return m_refCounter.decrementAndGet() == 0;
//		}
//
//		boolean isStored() {
//			return m_isStored.get();
//		}
//
//		void setStored() {
//			m_isStored.set(true);
//		}
//
//		Data<T> get() {
//			return m_data;
//		}
//
//		long getIndex() {
//			return m_index;
//		}
//	}
//
//	@Override
//	public void closeForAdding() {
//		m_delegate.closeForAdding();
//	}
//
//}
