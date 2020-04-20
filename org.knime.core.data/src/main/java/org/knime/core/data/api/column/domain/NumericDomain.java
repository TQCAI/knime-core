package org.knime.core.data.api.column.domain;

import org.knime.core.data.api.column.ReadAccess;

public interface NumericDomain<A extends ReadAccess> extends Domain {
	A getMin();

	A getMax();

	A getSum();
}
