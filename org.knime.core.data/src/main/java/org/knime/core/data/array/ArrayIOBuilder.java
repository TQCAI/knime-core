package org.knime.core.data.array;

import org.knime.core.data.array.io.ArrayIOs;

public interface ArrayIOBuilder {
	ArrayIOBuilder addDouble();

	ArrayIOBuilder startGroup();

	ArrayIOBuilder endGroup();

	ArrayIOs build();
}
