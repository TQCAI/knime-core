package org.knime.core.data.store.cache;

import java.util.Map;
import java.util.TreeMap;

import org.knime.core.data.Data;
import org.knime.core.data.DataReader;
import org.knime.core.data.DataWriter;

// Caches chunks of data
// either per column or per RowRecord

public class DataCache<D extends Data> {

	protected Map<Long, D> m_cache = new TreeMap<>();

	private long m_size = 0;
	private long m_flushIndex = 0;

	public void close() {
		clear();
		m_cache.clear();
	}

	/**
	 * @param array.
	 * 
	 *               TODO index is in because of 'async' put. will cause problems in
	 *               'flushNext(...)'. No idea if we should keep the index or not.
	 */
	public void put(D data) {
		data.retain();
		m_cache.put(m_size++, data);
	}

	/**
	 * Flush next array
	 * 
	 * TODO react on 'gaps' e.g. when data @ index is missing? We could also remove
	 * the index from the 'accept'
	 */
	void flushNext(boolean clear, DataWriter<D> consumer) {
		if (m_flushIndex < m_size) {
			final D data = m_cache.get(m_flushIndex);
			consumer.write(data);
			if (clear) {
				m_cache.remove(m_flushIndex);
				data.release();
			}
			m_flushIndex++;
		}
	}

	boolean isFullyFlushed() {
		return m_flushIndex == m_size;
	}

	public D getOrLoad(long index, DataReader<D> loader) {
		D data = m_cache.get(index);
		if (data == null) {
			data = loader.read(index);
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