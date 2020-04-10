package org.knime.core.data.array.types;

import org.knime.core.data.access.ReadableStringAccess;
import org.knime.core.data.access.WritableStringAccess;

public class StringArray implements Array<String[]> {

	private String[] m_array;

	StringArray(int capacity) {
		m_array = new String[capacity];
	}

	@Override
	public boolean isMissing(int index) {
		return m_array[index] == null;
	}

	@Override
	public void setMissing(int index) {
		m_array[index] = null;
	}

	@Override
	public String[] get() {
		return m_array;
	}

	public static final class NativeStringValue //
			extends AbstractNativeValue<String[], StringArray> //
			implements ReadableStringAccess, WritableStringAccess {

		@Override
		public String getStringValue() {
			return m_array[m_index].toString();
		}

		@Override
		public void setStringValue(final String value) {
			m_array[m_index] = value;
		}
	}

	@Override
	public void close() throws Exception {
		m_array = null;
	}

	@Override
	public long size() {
		return m_array.length;
	}
}
