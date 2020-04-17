package org.knime.core.data.store;

import java.io.Flushable;
import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

// TODO syncing on index
public class CachedDataSet<T, A extends DataAccess<T>> implements DataStore<T, A>, Flushable {

	private long m_size;
	private final Map<Long, Data<T>> m_data = new TreeMap<>();
	private final DataStore<T, A> m_delegate;

	CachedDataSet(DataStore<T, A> delegate) {
		m_delegate = delegate;
	}

	@Override
	public void add(Data<T> data) {
		data.retain();
		m_data.put(m_size++, data);
	}

	@Override
	public Data<T> get(long index) {
		Data<T> data = m_data.get(index);
		if (data == null) {
			data = m_delegate.get(index);
			m_data.put(index, data);
		}
		// if data was in cache we retain for external. If data was not yet in cache, we
		// retain for ourselves.
		data.retain();
		return data;
	}

	@Override
	public long size() {
		return m_size;
	}

	@Override
	public void closeForConsume() {
		m_delegate.closeForConsume();
	}

	@Override
	public void close() throws Exception {
		clearMemory();
		m_delegate.close();
	}

	@Override
	public void destroy() throws Exception {
		close();
		m_delegate.destroy();
	}

	@Override
	public void flush() throws IOException {
		for (final Data<T> data : m_data.values()) {
			// TODO check if we already flushed this data or not.
			m_delegate.add(data);
		}
		clearMemory();
	}

	private void clearMemory() {
		for (final Data<T> data : m_data.values()) {
			data.release();
		}
		m_data.clear();
	}

	@Override
	public A createAccess() {
		return m_delegate.createAccess();
	}

	@Override
	public Data<T> create() {
		return m_delegate.create();
	}

}
