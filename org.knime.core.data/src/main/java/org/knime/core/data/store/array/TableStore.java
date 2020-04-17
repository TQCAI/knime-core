package org.knime.core.data.store.array;

import java.io.Flushable;
import java.io.IOException;
import java.util.ArrayList;

import org.knime.core.data.api.column.ColumnType;

// TODO formerly known as buffer ;-)
public class TableStore implements Flushable {

	// TODO long...
	private ArrayList<ColumnStore<?>> m_columnStore;
	private ColumnType[] m_types;

	public TableStore(ColumnType[] types) {
		m_types = types;
		m_columnStore = new ArrayList<>((int) types.length);
		for (long l = 0; l < types.length; l++) {
			m_columnStore.add(new DefaultColumnStore<>());
		}
	}

	public ColumnType getColumnType(long index) {
		return m_types[(int) index];
	}

	public long getNumColumns() {
		return m_columnStore.size();
	}

	public ColumnStore<?> getColumnStore(long columnIndex) {
		return m_columnStore.get((int) columnIndex);
	}

	@Override
	public void flush() throws IOException {

	}

	// Implement as cache
	static class DefaultColumnStore<A extends Array> implements ColumnStore<A>, Flushable {

		DefaultColumnStore() {
		}

		@Override
		public void add(A array) {

		}

		@Override
		public A get(long index) {
			return null;
		}

		@Override
		public void closeForAdditions() {

		}

		@Override
		public long size() {
			return 0;
		}

		@Override
		public void flush() throws IOException {
			// TODO Auto-generated method stub
		}
	}

}
