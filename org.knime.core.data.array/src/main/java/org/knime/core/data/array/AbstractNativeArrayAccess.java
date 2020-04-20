
package org.knime.core.data.array;

import org.knime.core.data.data.Data;
import org.knime.core.data.store.DataAccess;

// TODO composition over inheritance? :-(
abstract class AbstractNativeArrayAccess<A, N extends Data<A>> //
		implements DataAccess<N> {

	protected int m_index = -1;

	private N m_array;

	protected A m_data;

	@Override
	public void incIndex() {
		m_index++;
	}

	@Override
	public void setData(final N array) {
		m_index = -1;
		m_array = array;
		m_data = m_array.get();
	}

	@Override
	public boolean isMissing() {
		return m_array.isMissing(m_index);
	}

	@Override
	public void setMissing() {
		m_array.setMissing(m_index);
	}
}
