package org.knime.core.data.array;

import org.knime.core.data.array.io.ArrayIOs;
import org.knime.core.data.array.types.DoubleArray;

public interface ArrayFactory {
	DoubleArray createDoubleArray();

	ArrayIOs createArrayIOBuilder();
}
