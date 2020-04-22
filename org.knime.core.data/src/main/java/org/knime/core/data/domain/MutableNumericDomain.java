package org.knime.core.data.domain;

import org.knime.core.data.column.ColumnType.NumericData;
import org.knime.core.data.value.NumericReadValue;

public interface MutableNumericDomain<D extends NumericData, A extends NumericReadValue>
		extends NumericDomain<A>, MutableDomain<D> {
}
