package org.knime.core.data.data.cache;

import java.util.Map;
import java.util.TreeMap;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataLoader;
import org.knime.core.data.data.DataWriter;

// TODO thread safetey etc etc
// TODO interface read/write?
class DataCache<D extends Data> {

	private long m_flushIndex = 0;
	protected Map<Long, D> m_cache = new TreeMap<>();
	private long m_size = 0;

	// Delegates
	private final DataWriter<D> m_writer;

	public DataCache(final DataWriter<D> writer) {
		m_writer = writer;
	}

	public DataCache() {
		m_writer = null;
	}

	public void close() {
		clear();
		m_cache.clear();
	}

	/**
	 * @param array.
	 */
	public void put(long index, D array) {
		array.retain();
		m_cache.put(m_size++, array);
	}

	/**
	 * Flush next array
	 * 
	 * TODO react on 'gaps' e.g. when data @ index is missing? We could also remove
	 * the index from the 'accept'
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