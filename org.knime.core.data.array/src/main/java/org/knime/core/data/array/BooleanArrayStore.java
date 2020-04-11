
package org.knime.core.data.array;

import org.knime.core.data.api.access.ReadableBooleanAccess;
import org.knime.core.data.api.access.WritableBooleanAccess;
import org.knime.core.data.array.BooleanArrayStore.BooleanArray;
import org.knime.core.data.array.BooleanArrayStore.BooleanArrayAccess;
import org.knime.core.data.store.types.BooleanDataStore;

public class BooleanArrayStore extends AbstractArrayStore<BooleanArray, BooleanArrayAccess>
		implements BooleanDataStore<BooleanArray, BooleanArrayAccess> {

	BooleanArrayStore(final long chunkSize) {
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

	static class BooleanArray extends AbstractNativeArray<boolean[]> {
		BooleanArray(final long capacity) {
			super(new boolean[(int) capacity], (int) capacity);
		}
	}

	final class BooleanArrayAccess//
			extends AbstractNativeArrayAccess<boolean[], BooleanArray> //
			implements ReadableBooleanAccess, WritableBooleanAccess {

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
