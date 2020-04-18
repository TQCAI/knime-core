package org.knime.core.data.array.table;

import java.util.ArrayList;

import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.PrimitiveType;
import org.knime.core.data.api.column.ReadColumn;
import org.knime.core.data.array.cache.ArrayCaches;
import org.knime.core.data.array.types.DoubleReadColumn;

//TODO interface / abstract super class for read/write
public class DefaultReadTable implements ReadTable {

	// TODO long...
	private ArrayList<ReadColumn<?>> m_columns;

	public DefaultReadTable(ArrayCaches group) {
		m_columns = new ArrayList<>(group.size());

		for (long l = 0; l < types.length; l++) {
			final PrimitiveType[] primitiveTypes = types[(int) l].getPrimitiveTypes();
			if (primitiveTypes.length == 1) {
				switch (primitiveTypes[0]) {
				case DOUBLE:
					m_columns.add(new DoubleReadColumn(store));
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
		return m_columns.size();
	}

	@Override
	public ReadColumn<?> getReadableColumn(long columnIndex) {
		return m_columns.get((int) columnIndex);
	}
}
