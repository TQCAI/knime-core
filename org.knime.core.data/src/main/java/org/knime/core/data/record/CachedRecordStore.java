package org.knime.core.data.record;

import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataWriter;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.store.DataReaderConfig;
import org.knime.core.data.store.cache.CachedDataStore;

class CachedRecordStore implements RecordStore {

	private final RecordStore m_delegate;

	// one cache for each column. use-case: two tables with different filters access
	// same table.
	private List<CachedDataStore<ColumnData, DataReaderConfig>> m_caches;

	private ColumnType<?, ?>[] m_types;

	public CachedRecordStore(final RecordStore delegate) {
		m_delegate = delegate;
		m_types = delegate.getColumnTypes();
		m_caches = new ArrayList<>();
		for (int i = 0; i < m_types.length; i++) {
			m_caches.add(null);
		}
	}

	@Override
	public RecordWriter getWriter() {
		// Write each column individually in cache
		return new RecordWriter() {
			@SuppressWarnings("unchecked")
			final DataWriter<ColumnData>[] m_writers = new DataWriter[m_types.length];
			{
				for (int i = 0; i < m_types.length; i++) {
					m_writers[i] = m_caches.get(i).getWriter();
				}
			}

			@Override
			public void write(final Record data) {
				final ColumnData[] columnData = data.getData();
				for (int i = 0; i < m_types.length; i++) {
					m_writers[i].write(columnData[i]);
				}
			}

			@Override
			public void close() {
				for (int i = 0; i < m_types.length; i++) {
					m_writers[i].close();
				}
			}
		};
	}

	@Override
	public RecordReader createReader(RecordReaderConfig config) {
		return null;
	}

	@Override
	public ColumnType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	@Override
	public void close() throws Exception {
		m_delegate.close();
	}

}
