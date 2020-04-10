
package org.knime.core.data.array;

import org.knime.core.data.access.ReadableBooleanAccess;
import org.knime.core.data.access.WritableBooleanAccess;
import org.knime.core.data.array.BooleanColumnStore.BooleanArray;
import org.knime.core.data.array.BooleanColumnStore.BooleanArrayAccess;
import org.knime.core.data.column.Domain;

public class BooleanColumnStore extends AbstractArrayColumnStore<BooleanArray, BooleanArrayAccess> {

	BooleanColumnStore(final long chunkSize) {
		super(chunkSize);
	}

	@Override
	public BooleanArrayAccess createDataAccess() {
		return new BooleanArrayAccess();
	}

	@Override
	protected BooleanArray create(final long capacity) {
		return new BooleanArray(capacity);
	}

	@Override
	protected Domain initDomain() {
		// TODO return empty NumericColumnDomain
		return null;
	}

	static class BooleanArray extends AbstractNativeArray<boolean[]> {

		BooleanArray(final long capacity) {
			super(new boolean[(int) capacity], (int) capacity);
		}
	}

	static final class BooleanArrayAccess//
		extends AbstractNativeArrayAccess<boolean[], BooleanArray> //
		implements ReadableBooleanAccess, WritableBooleanAccess
	{

		@Override
		public boolean getBooleanValue() {
			return m_data[m_index];
		}

		@Override
		public void setBooleanValue(final boolean value) {
			m_data[m_index] = value;
		}
	}
}
