package org.knime.core.data.api;

import org.knime.core.data.data.DataAccess;
import org.knime.core.data.data.types.DoubleData.DoubleAccess;

public interface PrimitiveType<A extends DataAccess<?>> {

	public A createAccess();

	final static class DoubleType implements PrimitiveType<DoubleAccess> {

		public static DoubleType INSTANCE = new DoubleType();

		private DoubleType() {
			// noop.
		}

		@Override
		public DoubleAccess createAccess() {
			return new DoubleAccess();
		}
	}
}
