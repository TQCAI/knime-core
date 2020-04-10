
package org.knime.core.data.array;

import org.knime.core.data.access.ReadableStringAccess;
import org.knime.core.data.access.WritableStringAccess;
import org.knime.core.data.array.StringColumnStore.StringArray;
import org.knime.core.data.array.StringColumnStore.StringArrayAccess;
import org.knime.core.data.column.Domain;

public class StringColumnStore extends AbstractArrayColumnStore<StringArray, StringArrayAccess> {

	StringColumnStore(final long chunkSize) {
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

	@Override
	protected Domain initDomain() {
		// TODO return empty nominal domain
		return null;
	}

	static class StringArray extends AbstractNativeArray<String[]> {

		StringArray(final long capacity) {
			super(new String[(int) capacity], (int) capacity);
		}
	}

	static final class StringArrayAccess//
		extends AbstractNativeArrayAccess<String[], StringArray> //
		implements ReadableStringAccess, WritableStringAccess
	{

		@Override
		public String getStringValue() {
			return m_data[m_index];
		}

		@Override
		public void setStringValue(final String value) {
			m_data[m_index] = value;
		}
	}
}
