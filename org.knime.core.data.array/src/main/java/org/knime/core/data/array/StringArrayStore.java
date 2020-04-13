
package org.knime.core.data.array;

import org.knime.core.data.api.access.ReadableStringAccess;
import org.knime.core.data.api.access.WritableStringAccess;
import org.knime.core.data.array.StringArrayStore.StringArray;
import org.knime.core.data.array.StringArrayStore.StringArrayAccess;
import org.knime.core.data.store.WritableDomain;
import org.knime.core.data.store.types.StringStore;

public class StringArrayStore extends AbstractArrayStore<StringArray, StringArrayAccess>
		implements StringStore<StringArray, StringArrayAccess> {

	StringArrayStore(final long chunkSize) {
		super(chunkSize);
	}

	@Override
	public StringArrayAccess createDataAccess() {
		return new StringArrayAccess();
	}

	@Override
	protected StringArray create(final long capacity) {
		return new StringArray(capacity);
	}

	static class StringArray extends AbstractNativeArray<String[]> {

		StringArray(final long capacity) {
			super(new String[(int) capacity], (int) capacity);
		}
	}

	static final class StringArrayAccess//
			extends AbstractNativeArrayAccess<String[], StringArray> //
			implements ReadableStringAccess, WritableStringAccess {

		@Override
		public String getStringValue() {
			return m_data[m_index];
		}

		@Override
		public void setStringValue(final String value) {
			m_data[m_index] = value;
		}
	}

	@Override
	public WritableDomain<StringArray> getDomain() {
		throw new UnsupportedOperationException("not yet supported");
	}
}
