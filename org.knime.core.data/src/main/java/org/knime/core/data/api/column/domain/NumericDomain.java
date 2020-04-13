package org.knime.core.data.api.column.domain;

public interface NumericDomain extends Domain {
	double getMin();

	double getMax();

	double getSum();
}
