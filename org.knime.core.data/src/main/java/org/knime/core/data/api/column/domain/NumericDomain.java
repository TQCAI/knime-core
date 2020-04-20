package org.knime.core.data.api.column.domain;

import org.knime.core.data.api.column.NumericReadAccess;

// TODO overdesigned?
public interface NumericDomain<A extends NumericReadAccess> extends Domain {
	A getMin();

	A getMax();

	A getSum();
}
