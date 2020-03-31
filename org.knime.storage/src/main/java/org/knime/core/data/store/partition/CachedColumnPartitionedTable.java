package org.knime.core.data.store.partition;

import java.io.IOException;

import org.knime.core.data.store.table.column.ColumnSchema;
import org.knime.core.data.store.table.column.ReadableColumnCursor;
import org.knime.core.data.store.table.column.ReadableTable;
import org.knime.core.data.store.table.column.WritableColumn;
import org.knime.core.data.store.table.column.WritableTable;

public class CachedColumnPartitionedTable<T> implements ReadableTable, WritableTable {

	// TODO we support 'Long'-many columns.
	private ColumnPartitionStore<T>[] m_columnPartitionStores;
	private Store<T> m_store;
	private WritablePartitionedColumn<T>[] m_writableColumn;

	public CachedColumnPartitionedTable(final ColumnSchema[] schema, final Store<T> store) throws IOException {
		m_store = store;
		for (int i = 0; i < schema.length; i++) {
			m_columnPartitionStores[i] = m_store.create(schema[(int) i].getType());
			m_writableColumn[i] = new WritablePartitionedColumn<T>(m_columnPartitionStores[i]);
		}

	}

	@Override
	public ReadableColumnCursor createReadableColumnCursor(long columnIndex) {
		return new ReadablePartitionedColumnCursor<T>(m_columnPartitionStores[(int) columnIndex]);
	}

	@Override
	public WritableColumn getWritableColumn(long columnIndex) {
		return m_writableColumn[(int) columnIndex];
	}

	@Override
	public long getNumColumns() {
		return m_columnPartitionStores.length;
	}

	@Override
	public void close() throws Exception {
		// TODO (Q: free all memory)?
		// TODO what are the semantics of this 'close'
	}

}
