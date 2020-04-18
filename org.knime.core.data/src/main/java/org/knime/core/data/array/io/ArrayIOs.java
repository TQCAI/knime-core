package org.knime.core.data.array.io;

import org.knime.core.data.array.Array;

public interface ArrayIOs {
	long size();

	<A extends Array> ArrayIO<A> addDouble();
}
