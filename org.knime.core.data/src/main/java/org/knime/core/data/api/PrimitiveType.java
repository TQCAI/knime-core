package org.knime.core.data.api;

import org.knime.core.data.api.column.domain.MutableDomain;
import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.types.DoubleData;
import org.knime.core.data.data.types.DoubleData.DoubleAccess;

// looks like an enum, but isn't! We want to support nesting, complex types etc.
public interface PrimitiveType<D extends Data, A extends DataAccess<?>> {

	public A createAccess();

	boolean hasDomain();

	MutableDomain<D> createEmptyDomain();

	final static class DoubleType implements PrimitiveType<DoubleData, DoubleAccess> {

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
