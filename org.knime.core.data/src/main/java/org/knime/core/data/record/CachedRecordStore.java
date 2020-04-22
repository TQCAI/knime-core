package org.knime.core.data.record;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnType;

/*
 * TODO implement cache on column-data level. Use-case two readers with different (but overlapping) column selections in RecordReaderConfig.  
 * 1. Write-case:  Flush is no problem, we always write the entire table at once (as of Apr 22 '20 ;-)).
 * 2. Read-case should should work as follows: Check what's in the cache and retain available data. Create new RecordBatchReader for missing data. Depending on some heuristics we can also read-in all requested columns in following requests. 
 * -- (Counter example: Cursor A requires 1 column, Cursor B 2,3,4,5,6,7,8... Cursor A is at chunk index 1, Cursor B at size-1. We should only read in 1,2,3,4,5,6,7,8 for the last request of Cursor B. It's a union of columns for each chunk index somehow.
 */
// TODO interface for cache
// TODO  async pre-load / pre flush? here or in the actual cursor?
// TODO thread-safety
public class CachedRecordStore implements RecordStore, Flushable {

	// one cache for each column. use-case: two tables with different filters access
	// same table.
	private List<Map<Integer, ColumnData>> m_caches;

	// all types
	private ColumnType<?, ?>[] m_types;

	// writer
	private RecordWriter m_writer;

	// size of cache
	private int m_size = 0;
	private int m_flushIndex = 0;

	private RecordReader m_reader;

	private RecordStore m_delegate;

	public CachedRecordStore(final RecordStore delegate) {
		m_types = delegate.getColumnTypes();
		m_delegate = delegate;
		m_caches = new ArrayList<>();
		for (int i = 0; i < m_types.length; i++) {
			m_caches.add(new TreeMap<Integer, ColumnData>());
		}

		// Only one writer. Maybe a property of the delegate
		m_writer = delegate.getWriter();

		// TODO we create more readers for parallel reading.
		m_reader = delegate.createReader();
	}

	@Override
	public RecordWriter getWriter() {
		// Write each column individually in the corresponding cache
		return new RecordWriter() {

			@Override
			public void write(final Record data) {
				final ColumnData[] columnData = data.getData();
				for (int i = 0; i < m_types.length; i++) {
					columnData[i].retain();
					m_caches.get(i).put(m_size, columnData[i]);
				}
				m_size++;
			}
		};
	}

	/**
	 * BIG TODO: implement flush differently for read-only scenario!!!!
	 */
	@Override
	public void flush() throws IOException {
		// in the writing scenario, we know that batches are complete, e.g. no columns
		// are missing. Let's just flush!

		// array to be re-used.

		final ColumnData[] data = new ColumnData[m_caches.size()];
		// TODO always flush fully? for async we have to refactor the while-loop
		for (; m_flushIndex < m_size; m_flushIndex++) {
			for (int i = 0; i < m_caches.size(); i++) {
				final Map<Integer, ColumnData> cache = m_caches.get(i);
				data[i] = cache.get(m_flushIndex);
			}
			// TODO reuse existing record object to avoid re-creation. later.
			m_writer.write(new Record(data));
			for (int i = 0; i < data.length; i++) {
				// once written, we can release our reference
				data[i].release();
				m_caches.get(i).clear();
			}
		}
	}

	// Move to CachedRecordReadStore. Make sure caches are 'lazily' instantiated in
	// case of read access.
	@Override
	public RecordReader createReader() {
		return new RecordReader() {

			@Override
			public int getNumChunks() {
				return m_size;
			}

			// TODO don't create a new object each time. reuse and update.
			// TODO thread-safety. what happens if after columnData.get() the data is
			// flushed and released?

			@Override
			public Record read(int chunkIndex, RecordReaderHints hints) {
				final int[] indices = hints.getColumnIndices();
				final ColumnData[] data = new ColumnData[indices.length];
				final BitSet bits = new BitSet(data.length);
				for (int i = 0; i < indices.length; i++) {
					final ColumnData columnData = m_caches.get(indices[i]).get(chunkIndex);
					if (columnData == null) {
						bits.clear(i);
					} else {
						data[i] = columnData;
						columnData.retain();
						bits.set(i);
					}
				}

				// TODO Now we could be nice to our friends also reading from this cache and
				// also load their data. Optimizes away additional IO call overhead to backend.
				// Implementation idea (1): keep list of readers and check
				// which reader is close behind us.
				// Implementation idea (2): for each reader at each chunk index keep the
				// superset of data which will likely be read at some point. Obviously we don't
				// want to keep objects for each index, so we have to be smarter somehow.

				final int numMissing = bits.cardinality() - bits.size();
				if (numMissing == 0) {
					return new Record(data);
				} else if (numMissing == indices.length) {
					final Record record = m_reader.read(chunkIndex, hints);
					final ColumnData[] readData = record.getData();
					for (int i = 0; i < data.length; i++) {
						// TODO do we need to put with index?
						readData[i].retain();
						m_caches.get(i).put(chunkIndex, readData[i]);
					}
					return record;
				} else {
					final int[] missing = new int[numMissing];
					int next = -1;
					for (int i = 0; i < numMissing; i++) {
						next = bits.nextClearBit(next + 1);
						missing[i] = next;
					}
					// subset of data to be stored.
					final Record tmp = m_reader.read(chunkIndex, new RecordReaderHints() {

						public int[] getColumnIndices() {
							return missing;
						}
					});
					for (int d = 0; d < missing.length; d++) {
						data[missing[d]] = tmp.getData()[d];
					}
					return new Record(data);
				}
			}
		};
	}

	@Override
	public ColumnType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	@Override
	public void close() throws Exception {
		// TODO close open reader/writer?
		m_delegate.close();
	}
}
