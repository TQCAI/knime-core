package org.knime.core.data.data;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.api.column.WriteAccess;
import org.knime.core.data.api.row.RowWriteCursor;
import org.knime.core.data.data.table.TableDataFactory;

public class DataRecordWriteCursor implements RowWriteCursor {

	// TODO hide all of that in a 'DataRecord' object? Would be additional,
	// unnecessary method calls.
	// TODO DataRecord would also help ensuring that all data have same capacity
	private final DataAccess<Data>[] m_accesses;
	private final DataFactory<Data>[] m_factories;
	private final DataConsumer<Data>[] m_consumer;
	private final int m_length;

	private int m_index;
	private int m_currentDataMaxIndex;

	private Data[] m_currentData;

	@SuppressWarnings("unchecked")
	public DataRecordWriteCursor(ConsumingDataStore store, final TableDataFactory factories) {
		NativeType<?, ?>[] m_nativeTypes = store.getColumnTypes();
		m_accesses = new DataAccess[m_nativeTypes.length];
		m_factories = new DataFactory[m_nativeTypes.length];
		m_consumer = new DataConsumer[m_nativeTypes.length];

		for (int i = 0; i < m_nativeTypes.length; i++) {
			m_accesses[i] = (DataAccess<Data>) m_nativeTypes[i].createAccess();
			m_factories[i] = (DataFactory<Data>) factories.getFactory(m_nativeTypes[i]);
			m_consumer[i] = store.getConsumer(i);
		}

		m_length = m_nativeTypes.length;
		m_currentData = new Data[m_nativeTypes.length];

		switchToNextData();
	}

	@Override
	public void close() throws Exception {
		releaseCurrentData();
	}

	@Override
	public WriteAccess[] get() {
		// TODO do we really, really need long column access? we could save many, many
		// casts.
		return m_accesses;
	}

	@Override
	public void fwd() {
		if (++m_index > m_currentDataMaxIndex) {
			switchToNextData();
			m_index = 0;
		}

		// TODO one forward per column. Required, as we don't have access to columnar
		// data directly! :-(
		for (int i = 0; i < m_length; i++) {
			m_accesses[i].fwd();
		}
	}

	private void releaseCurrentData() {
		for (int i = 0; i < m_length; i++) {
			m_consumer[i].accept(m_currentData[i]);
			m_currentData[i].release();
		}
	}

	private void switchToNextData() {
		try {
			releaseCurrentData();
			for (int i = 0; i < m_length; i++) {
				m_currentData[i] = m_factories[i].create();
				m_accesses[i].update(m_currentData[i]);
			}
			
			// m_currentData[0] required as we don't have a 'DataRecord'. DataRecord would also ensure all data have same length.
			m_currentDataMaxIndex = m_currentData[0].getMaxCapacity() - 1;
		} catch (final Exception e) {
			// TODO
			throw new RuntimeException(e);
		}
	}

	@Override
	public boolean canFwd() {
		return true;
	}
}