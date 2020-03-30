
package org.knime.core.data.store.vec.arrow;

import org.apache.arrow.vector.FieldVector;
import org.knime.core.data.store.vec.rw.WritableVectorAccess;

public abstract class AbstractArrowVecWriteAccess<V extends FieldVector> //
	extends AbstractArrowVecAccess<V> //
	implements WritableVectorAccess
{

	public AbstractArrowVecWriteAccess(final V vector) {
		super(vector);
	}

	@Override
	public void setMissing() {
		m_vector.setValueCount(m_idx + 1);
	}
}
