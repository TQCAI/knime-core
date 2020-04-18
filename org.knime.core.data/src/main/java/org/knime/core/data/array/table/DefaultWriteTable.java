package org.knime.core.data.array.table;

import java.util.ArrayList;

import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.PrimitiveType;
import org.knime.core.data.api.column.WritableAccess;
import org.knime.core.data.api.column.WriteColumn;
import org.knime.core.data.array.ArrayFactory;
import org.knime.core.data.array.types.DoubleWriteColumn;

//TODO interface / abstract super class for read/write
public class DefaultWriteTable implements WriteTable {

	// TODO long...
	private ArrayList<WriteColumn<?>> m_columns;
	private ColumnType[] m_types;

	// TODO on write we create a RW cache (because we want to potentially reuse it
	// later). How is cache passed from Write -> Read without knowing it's a cache?
	// (WriteColumn -> ReadColumn?)
	public DefaultWriteTable(final ColumnType[] types, final ArrayFactory factory) {
		m_types = types;
		m_columns = new ArrayList<>((int) types.length);

		// create a new cache
		for (long l = 0; l < types.length; l++) {
			final PrimitiveType[] primitiveTypes = types[(int) l].getPrimitiveTypes();
			if (primitiveTypes.length == 1) {
				switch (primitiveTypes[0]) {
				case DOUBLE:
					m_columns.add(new DoubleWriteColumn(() -> factory.createDoubleArray(), io.addDouble()));
					break;
				default:
					break;
				}
			} else {
				throw new UnsupportedOperationException("nyi");
			}
		}
	}

	@Override
	public long getNumColumns() {
		return m_types.length;
	}

	@Override
	public WriteColumn<? extends WritableAccess> getWritableColumn(long columnIndex) {
		return m_columns.get((int) columnIndex);
	}

	@Override
	public void close() throws Exception {
		// TODO close IO?
	}

}
