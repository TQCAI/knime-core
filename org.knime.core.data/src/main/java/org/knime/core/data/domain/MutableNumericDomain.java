package org.knime.core.data.domain;

import org.knime.core.data.access.NumericReadAccess;
import org.knime.core.data.column.NumericData;

public interface MutableNumericDomain<D extends NumericData, A extends NumericReadAccess>
		extends NumericDomain<A>, MutableDomain<D> {

}
