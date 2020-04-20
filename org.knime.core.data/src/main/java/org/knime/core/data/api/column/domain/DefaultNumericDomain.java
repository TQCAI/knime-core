package org.knime.core.data.api.column.domain;

import org.knime.core.data.data.types.DoubleData;

public class DefaultNumericDomain implements NumericDomain {

	private DoubleData m_array;

	public DefaultNumericDomain(DoubleData array) {
		m_array = array;
	}

	@Override
	public long getNumMissing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getNumNonMissing() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMin() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMax() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSum() {
		// TODO Auto-generated method stub
		return 0;
	}

}
