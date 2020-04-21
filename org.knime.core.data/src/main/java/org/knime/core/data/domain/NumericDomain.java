package org.knime.core.data.domain;

import org.knime.core.data.access.NumericReadAccess;

// TODO overdesigned?
public interface NumericDomain<A extends NumericReadAccess> extends Domain {
	A getMin();

	A getMax();

	A getSum();
}
