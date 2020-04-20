package org.knime.core.data.api.column.domain;

import org.knime.core.data.api.column.NumericReadAccess;
import org.knime.core.data.data.types.NumericData;

public interface MutableNumericDomain<D extends NumericData, A extends NumericReadAccess>
		extends NumericDomain<A>, MutableDomain<D> {

}
