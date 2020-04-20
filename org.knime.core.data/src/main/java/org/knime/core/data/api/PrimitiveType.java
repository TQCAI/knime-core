package org.knime.core.data.api;

import org.knime.core.data.data.Data;
import org.knime.core.data.data.DataAccess;

public enum PrimitiveType {
	STRING, DOUBLE, BOOLEAN;

	public <D extends Data> DataAccess<D> access() {
		// TODO Auto-generated method stub
		return null;
	}
}
