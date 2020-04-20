package org.knime.core.data.api;

import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.types.DoubleData.DoubleAccess;

// looks like an enum, but isn't! We want to support nesting, complex types etc.
public interface PrimitiveType<A extends DataAccess<?>> {

	public A createAccess();

	final static class DoubleType implements PrimitiveType<DoubleAccess> {

		public static DoubleType INSTANCE = new DoubleType();

		private DoubleType() {
		}

		@Override
		public DoubleAccess createAccess() {
			return new DoubleAccess();
		}
	}
}
