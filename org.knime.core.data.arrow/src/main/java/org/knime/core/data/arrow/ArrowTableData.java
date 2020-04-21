package org.knime.core.data.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;
import org.knime.core.data.DataFactory;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.ColumnBatchWriter;
import org.knime.core.data.record.RecordFormat;

public class ArrowTableData implements RecordFormat {

	private final ColumnType<?, ?>[] m_types;
	private final int m_chunkSize;

	// TODO one child-allocator per column?
	private BufferAllocator m_allocator;

	ArrowTableData(BufferAllocator allocator, ColumnType<?, ?>[] types, int chunkSize) {
		m_chunkSize = chunkSize;
		m_types = types;
		m_allocator = allocator;
	}

	@Override
	public TableDataReadAccess getReadAccess() {
		return null;
	}

	@Override
	public TableDataWriteAccess getWriteAccess() {
		return new TableDataWriteAccess() {

			@Override
			public <D extends Data> ColumnBatchWriter<D> getWriter() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

	@Override
	public TableDataFactory getFactory() {
		return new TableDataFactory() {

			@Override
			public <D extends Data, A extends DataAccess<D>> DataFactory<D> getDataFactory(ColumnType<D, A> type) {
				// TODO abstract that somewhere and only 'force' back-end to implement
				// doubleArray() etc.
				if (type instanceof ColumnType.DoubleType) {
					// cast considered to be safe by contract.
					@SuppressWarnings("unchecked")
					final DataFactory<D> factory = (DataFactory<D>) (() -> (D) new Float8VectorData(m_allocator,
							m_chunkSize));
					return factory;
				} else {
					throw new UnsupportedOperationException("Unknown primitive type " + type.toString());
				}
			}
		};
	}

	@Override
	public long getNumColumns() {
		return m_types.length;
	}

	@Override
	public ColumnType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	@Override
	public void close() throws Exception {
		m_allocator.close();
	}

}
