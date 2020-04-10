package org.knime.core.data.chunked;

import org.knime.core.data.Data;
import org.knime.core.data.DataAccess;
import org.knime.core.data.DataFactory;
import org.knime.core.data.DataWriter;

public class DefaultChunkedDataStore<T> implements ChunkedDataStore<T, Data<T>, DataAccess<T>> {

	private DataFactory<T> m_factory;
	private DataWriter<T> m_writer;
	private long m_numDataChunks;

	public DefaultChunkedDataStore(final DataFactory<T> factory) {
		m_factory = factory;
		m_writer = m_factory.createWriter();
	}

	@Override
	public DataAccess<T> createDataAccess() {
		return m_factory.createAccess();
	}

	@Override
	public void addData(Data<T> data) {
		m_writer.write(data);
		m_numDataChunks++;
	}

	@Override
	public Data<T> createData() {
		return m_factory.createData();
	}

	@Override
	public ChunkedDataCursor<T, Data<T>> cursor() {
		return new ChunkedDataCursor<T, Data<T>>() {

			private long m_index = -1;

			@Override
			public Data<T> get() {
				return null;
			}

			@Override
			public void fwd() {
				m_index++;
			}

			@Override
			public boolean canFwd() {
				return false;
			}

			@Override
			public void close() throws Exception {

			}
		};
	}

}
