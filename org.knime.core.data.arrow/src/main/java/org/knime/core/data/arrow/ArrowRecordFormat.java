package org.knime.core.data.arrow;

import java.io.File;
import java.io.IOException;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.Float8Vector;
import org.knime.core.data.column.ColumnData;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.Record;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordFormat;
import org.knime.core.data.record.RecordReader;
import org.knime.core.data.record.RecordReaderHints;
import org.knime.core.data.record.RecordStore;
import org.knime.core.data.record.RecordStoreHints;
import org.knime.core.data.record.RecordWriter;

public class ArrowRecordFormat implements RecordFormat {

	private final int m_chunkSize;
	private final BufferAllocator m_allocator;

	public ArrowRecordFormat(int chunkSize) {
		m_chunkSize = chunkSize;
		m_allocator = new RootAllocator();
	}

	// For debugging
	public void close() throws Exception {
		m_allocator.close();
	}

	@Override
	public RecordStore create(ColumnType<?, ?>[] schema, File file, RecordStoreHints hints) {
		return new ArrowRecordStore(file, schema, hints);
	}

	@Override
	public RecordFactory createFactory(ColumnType<?, ?>[] schema) {
		return new RecordFactory() {
			{
				// TODO initialize factories. could be expensive with nested types.
			}

			@Override
			public Record create() {
				final ColumnData[] data = new ColumnData[schema.length];
				for (int i = 0; i < schema.length; i++) {
					data[i] = new Float8VectorData(m_allocator, m_chunkSize);
				}
				return new Record(data);
			}

			@Override
			public ColumnType<?, ?>[] getColumnTypes() {
				return schema;
			}
		};
	}

	class ArrowRecordStore implements RecordStore {

		private final ColumnType<?, ?>[] m_types;
		private final FieldVectorWriter m_writer;
		private final File m_file;

		ArrowRecordStore(File file, final ColumnType<?, ?>[] types, RecordStoreHints hints) {
			m_types = types;
			m_file = file;
			m_writer = new FieldVectorWriter(file);
		}

		@Override
		public RecordWriter getWriter() {
			return new RecordWriter() {
				// TODO type on Record on FieldVector?
				@Override
				public void write(Record record) {
					try {
						final FieldVector[] data = (FieldVector[]) record.getData();
						m_writer.flush(data);
					} catch (IOException e) {
						// TODO
						throw new RuntimeException(e);
					}
				}
			};
		}

		@Override
		public RecordReader createReader() {
			return new RecordReader() {
				final FieldVectorReader m_reader;
				{
					try {
						m_reader = new FieldVectorReader(m_file, m_allocator);
					} catch (IOException e) {
						// TODO
						throw new RuntimeException(e);
					}
				}

				@Override
				public Record read(int chunkIndex, RecordReaderHints hints) {
					try {
						final FieldVector[] vectors = m_reader.read(chunkIndex);
						final ColumnData[] data = new ColumnData[vectors.length];
						for (int i = 0; i < data.length; i++) {
							// TODO wrapper for all types and nested types etc
							data[i] = new Float8VectorData((Float8Vector) vectors[0]);
						}
						return new Record(data);
					} catch (IOException e) {
						// TODO
						throw new RuntimeException(e);
					}
				}

				@Override
				public int getNumChunks() {
					return m_reader.size();
				}
			};
		}

		@Override
		public ColumnType<?, ?>[] getColumnTypes() {
			return m_types;
		}

		@Override
		public void close() throws Exception {
			m_writer.close();
		}
	}

}
