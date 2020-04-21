package org.knime.core.data;

import java.io.File;
import java.util.Map;

import org.knime.core.data.column.ColumnReadableTable;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.ColumnWriteTable;
import org.knime.core.data.domain.Domain;
import org.knime.core.data.record.Record;
import org.knime.core.data.record.RecordFactory;
import org.knime.core.data.record.RecordFormat;
import org.knime.core.data.row.RowWriteTable;
import org.knime.core.data.store.LoadingDataStore;
import org.knime.core.data.store.cache.CachedDataReadStore;
import org.knime.core.data.store.cache.CachedDataStore;

public class APIMock {

	public BufferedDataTable execute(DataTableSpec spec, ExecutionContext ctx) throws Exception {
		final TableContainer container = ctx.createTableContainer(spec);
		BufferedDataTable close = container.close();
		return close;
	}

	public void readTableFromDisc() {
		// deserialize file & domains from somewhere
		File f = null;

		// Deserialize from somewhere
		Map<Long, Domain> domains = null;
		ColumnType<?, ?>[] types = null;

		// TODO make sure we load the right version of 'TableData'
		RecordFormat store = null;

		// TODO attach memory listeners
		// TODO add to managed caches.
		LoadingDataStore cache = new CachedDataReadStore(types, store.getReadAccess());

		// we got our table back
		// TODO we only need a 'Read' Cache here.
		ColumnReadableTable table = TableUtils.create(cache);
	}

	// all in memory case
	class ExecutionContext {
		public TableContainer createTableContainer(DataTableSpec spec) {

			// New temporary file
			File f = null;

			// new ArrowTableIO(), later e.g. from extension point
			// TODO PrimitiveTypes are associcated with an array type.
			// TODO Support for 'Grouped/Struct' native types (-> struct)
			// TODO add config for individual columns (dict encoding, domain etc)
			ColumnType<?, ?>[] types = translate(spec);

			// Store to read/write data
			// TODO create with primitive types. contract: delivers the correct
			// loader/writer/factory according to PrimitiveTypes (e.g. double -> DoubleData,
			// byte[] -> ByteArrayData, etc).

			// TODO framework has to make sure to be able to serialize and deserialize the
			// TableData object. We'll create TableDataV2, TableDataV3, ... in the future
			// (backwards compatibility).
			RecordFormat data = null;

			// Let's cache the store to gain some performance
			// TODO register to memory alerts
			// TODO register to global LRU cache
			// TODO use factory method to create cache (we may want to change the cache in
			// the future).
			CachedDataStore store = new CachedDataStore(types, data.getWriter(), data.getReadAccess());

			// TODO add unique value checker etc.
			// TODO async computation of adapters. data can be added to cached before all
			// adapters are ready (?).
			// TODO test design with implementing dictionary encoding either as adapter OR
			// in DataAccess? Do we
			// need dictionary encoding at all for in-memory representation or is parquet
			// good enough?
			// -> IMPLEMENT DOMAIN
			
			// add decorators per column

			// Creates a writer to write columns of a table
			// wrap table into a TableContainer for outside access
			TableContainer container = new TableContainer() {

				// TODO return whatever we declare as API here
				// A table which can be filled with data.
				private ColumnWriteTable columnWriteTable = TableUtils.createColumnWriteTable(adapted,
						data.getFactory());

				final RecordFactory factory = null;
				final DataWriter<Record> consumer = null;

				private RowWriteTable rowWriteTable = TableUtils.createRowWriteTable(factory, consumer);

				// Similar to current API we close the container and with that create a
				// BufferedDataTable.
				@Override
				public BufferedDataTable close() throws Exception {
					// all data has been persisted. Close all writers!
					// should already be closed (WriteColumn.close())

					// TODO get reader from writer instead? reader=writer?
					// TODO maybe the ArrayIO is versioned and NOT the reader/writers themselves.

					ColumnReadableTable readTable = TableUtils.create(store, store.createReader());

					// return some wrapped BufferedDataTable providing access to data, e.g. through
					// ReadTable table = TableUtils.create(reader, null);
					// TODO respect filters etc.
					return null;
				}
			};

			return null;
		}

		private ColumnType<?, ?>[] translate(DataTableSpec spec) {
			return null;
		}
	}

	/**
	 * Execution Context point of view
	 */

	interface BufferedDataTable {
		/* TODO whatever is our API here later with iteratorUnsafe etc */
		ColumnReadableTable getReadTable();
	}

	interface DataTableSpec {

	}

	interface DataColumnSpec {

	}

	interface TableContainer {
		BufferedDataTable close() throws Exception;
	}
}
