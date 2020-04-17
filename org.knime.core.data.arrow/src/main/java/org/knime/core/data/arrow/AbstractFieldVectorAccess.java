
package org.knime.core.data.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.store.DataAccess;

abstract class AbstractFieldVectorAccess<V extends FieldVector> //
		implements DataAccess<V> {

	protected int m_index = -1;

	protected V m_vector;

	@Override
	public void incIndex() {
		m_index++;
	}

	@Override
	public void setData(final V vector) {
		m_index = -1;
		m_vector = vector;
	}

	@Override
	public boolean isMissing() {
		return m_vector.isNull(m_index);
	}

	@Override
	public void setMissing() {
		// TODO: Is this actually correct (especially when reusing the vector)? Or
		// use setNull instead? knime-python does it like here. But seems to be an
		// expensive operation!
		m_vector.setValueCount(m_index + 1);
	}
}
