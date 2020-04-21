package org.knime.core.data.column;

import org.knime.core.data.DoubleData;
import org.knime.core.data.DoubleData.DoubleAccess;
import org.knime.core.data.domain.MutableDomain;

// looks like an enum, but isn't! We want to support nesting, complex types etc.
public interface ColumnType<D extends ColumnData, A extends ColumnDataAccess<D>> {

	A createAccess();

	// TODO Interface 'NativeTypeWithDomain'
	boolean hasDomain();

	MutableDomain<D> createEmptyDomain();

	final static class DoubleType implements ColumnType<DoubleData, DoubleAccess> {

		public static DoubleType INSTANCE = new DoubleType();

		private DoubleType() {
		}

		@Override
		public DoubleAccess createAccess() {
			return new DoubleAccess();
		}

		@Override
		public boolean hasDomain() {
			return true;
		}

		@Override
		public MutableDomain<DoubleData> createEmptyDomain() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
