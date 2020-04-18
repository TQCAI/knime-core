package org.knime.core.data.array.cache;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.knime.core.data.array.Array;
import org.knime.core.data.array.ArrayReadStore;
import org.knime.core.data.array.ArrayWriteStore;

public class ArrayCaches implements Flushable {

	private final List<ArrayReadWriteCache<?>> m_caches;

	public ArrayCaches() {
		m_caches = new ArrayList<>();
		// TODO implement 'pre-flushing' and 'pre-loading'
	}

	public <A extends Array> ArrayReadWriteCache<A> cache(ArrayReadStore<A> read, ArrayWriteStore<A> write) {
		final ArrayReadWriteCache<A> cache = new ArrayReadWriteCache<>(read, write);
		m_caches.add(cache);
		return cache;
	}

	@Override
	public void flush() throws IOException {
		// TODO we can implement multiple strategies here:
		// flush by column, flush by record batch, flush by ...
		// we can also use additional information for the order of flush (e.g. which
		// column is actually still being used -> ref counting).
		for (final ArrayReadWriteCache<?> cache : m_caches) {
			if (!cache.isFlushed())
				cache.flushNext(true);
		}
	}

	public long size() {
		return m_caches.size();
	}

	// TODO thread safety
	public static class ArrayReadWriteCache<A extends Array> extends ArrayReadCache<A> implements ArrayWriteStore<A> {

		private long m_flushIndex = 0;
		private long m_size = 0;

		// Delegates
		private final ArrayWriteStore<A> m_writeStore;

		public ArrayReadWriteCache(final ArrayReadStore<A> read, final ArrayWriteStore<A> write) {
			super(read);
			m_writeStore = write;
		}

		/**
		 * @param array.
		 */
		@Override
		public void add(A array) {
			array.retain();
			m_cache.put(m_size++, array);
		}

		/**
		 * Flush next array
		 */
		void flushNext(boolean clear) {
			if (m_flushIndex < m_size) {
				A a = m_cache.get(m_flushIndex);
				m_writeStore.add(a);
				if (clear) {
					m_cache.remove(m_flushIndex);
					a.release();
				}
				m_flushIndex++;
			}
		}

		boolean isFlushed() {
			return m_flushIndex == m_size;
		}

		@Override
		public void closeWriteStore() {
			m_writeStore.closeWriteStore();
			// TODO block cache?
		}
	}

	// TODO thread safety
	public static class ArrayReadCache<A extends Array> implements ArrayReadStore<A> {

		protected Map<Long, A> m_cache = new TreeMap<>();

		private long m_size = 0;

		// Delegates
		private final ArrayReadStore<A> m_readStore;

		public ArrayReadCache(final ArrayReadStore<A> read) {
			m_readStore = read;
		}

		@Override
		public A get(long index) {
			A array = m_cache.get(index);
			if (array == null) {
				array = m_readStore.get(index);
				m_cache.put(index, array);
			}
			array.retain();
			return array;
		}

		@Override
		public long size() {
			return m_size;
		}

		@Override
		public void closeReadStore() {
			m_cache.clear();
			m_readStore.closeReadStore();
		}
	}
}
