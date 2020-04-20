package org.knime.core.data.arrow;

import org.apache.arrow.memory.BufferAllocator;
import org.knime.core.data.api.NativeType;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.DataFactory;
import org.knime.core.data.data.table.ColumnBatchWriter;
import org.knime.core.data.data.table.TableData;
import org.knime.core.data.data.table.TableDataFactory;
import org.knime.core.data.data.table.TableDataReadAccess;
import org.knime.core.data.data.table.TableDataWriteAccess;

public class ArrowTableData implements TableData {

	private final NativeType<?, ?>[] m_types;
	private final int m_chunkSize;

	// TODO one child-allocator per column?
	private BufferAllocator m_allocator;

	ArrowTableData(BufferAllocator allocator, NativeType<?, ?>[] types, int chunkSize) {
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
			public <D extends Data, A extends DataAccess<D>> DataFactory<D> getFactory(NativeType<D, A> type) {
				// TODO abstract that somewhere and only 'force' back-end to implement
				// doubleArray() etc.
				if (type instanceof NativeType.DoubleType) {
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
	public NativeType<?, ?>[] getColumnTypes() {
		return m_types;
	}

	@Override
	public void close() throws Exception {
		m_allocator.close();
	}

}
