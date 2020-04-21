package org.knime.core.data.domain;

import org.knime.core.data.NumericData;
import org.knime.core.data.access.NumericReadAccess;

public interface MutableNumericDomain<D extends NumericData, A extends NumericReadAccess>
		extends NumericDomain<A>, MutableDomain<D> {

}
