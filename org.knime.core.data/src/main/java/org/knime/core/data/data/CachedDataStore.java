package org.knime.core.data.data;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.knime.core.data.data.table.TableData;

// TODO interface
// TODO thread-safety
// TODO implement 'pre-flushing' and 'pre-loading'
public final class CachedDataStore implements Flushable, DataStore {

	private final List<DataCache<?>> m_caches;
	private final TableData m_data;

	public CachedDataStore(TableData data) {
		m_data = data;
		m_caches = new ArrayList<>((int) m_data.getNumColumns());

		for (int i = 0; i < m_data.getNumColumns(); i++) {
			m_caches.add(new DataCache<>(m_data.getWriter(i)));
		}
	}

	@Override
	public <D extends Data> DataConsumer<D> getConsumer(long columnIndex) {
		return new DataConsumer<D>() {

			private final DataWriter<D> m_writer;

			{
				m_writer = m_data.getWriter(columnIndex);
			}

			@Override
			public void accept(long index, D data) {
				@SuppressWarnings("unchecked")
				DataCache<D> cache = (DataCache<D>) m_caches.get((int) index);
				cache.accept(index, data);
			}

			@Override
			public void close() throws Exception {
				// close writer (we only consider single, non-parallel write for now)
				m_writer.close();
			}
		};
	}

	@Override
	public <D extends Data> DataLoader<D> getLoader(long columnIndex) {
		return new DataLoader<D>() {

			// create new delegate per loader
			private final DataLoader<D> m_loader;

			{
				m_loader = m_data.createLoader(columnIndex);
			}

			@Override
			public D load(long index) {
				@SuppressWarnings("unchecked")
				final DataCache<D> cache = (DataCache<D>) m_caches.get((int) index);
				return cache.getOrLoad(index, m_loader);
			}

			@Override
			public long size() {
				return m_loader.size();
			}

			@Override
			public void close() throws Exception {
				m_loader.close();
			}
		};
	}

	public void flush() throws IOException {
		// TODO we can implement multiple strategies here:
		// flush by column, flush by record batch, flush by ...
		// we can also use additional information for the order of flush (e.g. which
		// column is actually still being used -> ref counting).
		for (final DataCache<?> cache : m_caches) {
			if (!cache.isFlushed())
				cache.flushNext(true);
		}

		clear();
	}

	@Override
	public void close() throws Exception {
		m_data.close();
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).close();
		}
	}

	// release all data from caches. keeps caches open until close
	private void clear() {
		for (int i = 0; i < m_caches.size(); i++) {
			m_caches.get(i).clear();
		}
	}

	// TODO thread safety
	static class DataCache<D extends Data> {

		private long m_flushIndex = 0;
		protected Map<Long, D> m_cache = new TreeMap<>();
		private long m_size = 0;

		// Delegates
		private final DataWriter<D> m_writer;

		public DataCache(final DataWriter<D> writer) {
			m_writer = writer;
		}

		public void close() {
			clear();
			m_cache.clear();
		}

		/**
		 * @param array.
		 */
		public void accept(long index, D array) {
			array.retain();
			m_cache.put(m_size++, array);
		}

		/**
		 * Flush next array
		 */
		void flushNext(boolean clear) {
			if (m_flushIndex < m_size) {
				D a = m_cache.get(m_flushIndex);
				m_writer.write(a);
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

		public D getOrLoad(long index, DataLoader<D> reader) {
			D data = m_cache.get(index);
			if (data == null) {
				data = reader.load(index);
				m_cache.put(index, data);
			}
			data.retain();
			return data;
		}

		public long size() {
			return m_size;
		}

		void clear() {
			for (final D data : m_cache.values()) {
				data.release();
			}
		}

	}
}
