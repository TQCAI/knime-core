package org.knime.core.data.record;

import java.io.Flushable;
import java.util.ArrayList;
import java.util.List;

import org.knime.core.data.DataReader;
import org.knime.core.data.DataWriter;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.store.cache.CachedDataStore;
import org.knime.core.data.store.cache.DataCache;

class CachedRecordStore implements RecordStore, Flushable {

	private final RecordStore m_delegate;

	// one cache for each column. use-case: two tables with different filters access
	// same table.
	private List<DataCache<ColumnData>> m_caches;

	// all types
	private ColumnType<?, ?>[] m_types;

	public CachedRecordStore(final RecordStore delegate) {
		m_delegate = delegate;
		m_types = delegate.getColumnTypes();
		m_caches = new ArrayList<>();
		for (int i = 0; i < m_types.length; i++) {
			m_caches.add(new DataCache<>());
		}
	}

	@Override
	public RecordWriter getWriter() {
		// Write each column individually in cache
		return new RecordWriter() {

			@Override
			public void write(final Record data) {
				final ColumnData[] columnData = data.getData();
				for (int i = 0; i < m_types.length; i++) {
					m_caches.get(i).put(columnData[i]);
				}
			}

			@Override
			public void close() {
				// Nothing?
			}
		};
	}

	// Move to CachedRecordReadStore. Make sure caches are 'lazily' instantiated in
	// case of read access.
	@Override
	public RecordReader createReader(RecordReaderConfig config) {
		return new RecordReader() {

			final int[] m_colIndices = config.getColumnIndices();
			@SuppressWarnings("unchecked")
			final DataCache<ColumnData>[] m_selectedCaches = new DataCache[m_colIndices.length];
			{
				int j = 0;
				for (int i = 0; i < m_types.length; i++) {
					if (i == m_colIndices[j]) {
						m_selectedCaches[i] = m_caches.get(i);
						if (++j == m_colIndices.length) {
							break;
						}
					}
				}
			}

			@Override
			public void close() throws Exception {
				for (int i = 0; i < m_colIndices.length; i++) {
					m_caches[m_colIndices[i]].close();
				}
			}

			@Override
			public long size() {
				return m_caches[m_colIndices[0]]
			}

			@Override
			public Record read(long index) {
				// TODO reuse array.
				final ColumnData[] data = new ColumnData[m_colIndices.length];
				for (int i = 0; i < m_colIndices.length; i++) {
					data[i] = m_selectedCaches[m_colIndices[i]].getOrLoad(index, ????????);
				}

				// TODO don't create a new object each time. reuse and update.
				return new Record(data);
			}
		};
	}

	@Override
	public ColumnType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	@Override
	public RecordReader createReader() {
		// TODO
		return createReader(null);
	}

}
