package org.knime.core.data;

import java.io.File;
import java.util.Map;

import org.knime.core.data.api.PrimitiveType;
import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteableTable;
import org.knime.core.data.api.column.domain.Domain;
import org.knime.core.data.data.ConsumingDataStore;
import org.knime.core.data.data.DataDomainAdapter;
import org.knime.core.data.data.DataStores;
import org.knime.core.data.data.LoadingDataStore;
import org.knime.core.data.data.cache.CachedDataStore;
import org.knime.core.data.data.cache.CachedLoadingDataStore;
import org.knime.core.data.data.table.TableData;
import org.knime.core.data.data.table.TableUtils;

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
		PrimitiveType[] types = null;

		// TODO make sure we load the right version of 'TableData'
		TableData store = null;

		// TODO attach memory listeners
		// TODO add to managed caches.
		LoadingDataStore cache = new CachedLoadingDataStore(types, store.getReadAccess());

		// we got our table back
		// TODO we only need a 'Read' Cache here.
		ReadTable table = TableUtils.create(cache);
	}

	// all in memory case
	class ExecutionContext {
		public TableContainer createTableContainer(DataTableSpec spec) {

			// New temporary file
			File f = null;

			// new ArrowTableIO(), later e.g. from extension point
			// TODO PrimitiveTypes are associcated with an array type.
			// TODO Support for 'Grouped' primitive types (-> struct)
			// TODO add config for individual columns (dict encoding, domain etc)
			PrimitiveType<?, ?>[] types = translate(spec);

			// Store to read/write data
			// TODO create with primitive types. contract: delivers the correct
			// loader/writer/factory according to PrimitiveTypes (e.g. double -> DoubleData,
			// byte[] -> ByteArrayData, etc).
			//
			// TODO framework has to make sure to be able to serialize and deserialize the
			// TableData object. We'll create TableDataV2, TableDataV3, ... in the future
			// (backwards compatibility).
			TableData data = null;

			// Let's cache the store to gain some performance
			// TODO register to memory alerts
			// TODO register to global LRU cache
			// TODO use factory method to create cache (we may want to change the cache in
			// the future).
			final CachedDataStore store = new CachedDataStore(types, data.getWriteAccess(), data.getReadAccess());

			// TODO add unique value checker etc.
			final DataDomainAdapter domainStore = new DataDomainAdapter(types);
			final ConsumingDataStore adapted = DataStores.addAdapter(store, domainStore);

			// add decoraters per column
			// TODO domain
			// TODO unique value checking

			// Creates a writer to write columns of a table
			// wrap table into a TableContainer for outside access
			final TableContainer container = new TableContainer() {

				// TODO return whatever we declare as API here
				// A table which can be filled with data.
				private WriteableTable writeTable = TableUtils.create(adapted, data.getFactory());

				// Similar to current API we close the container and with that create a
				// BufferedDataTable.
				@Override
				public BufferedDataTable close() throws Exception {
					// all data has been persisted. Close all writers!
					// should already be closed (WriteColumn.close())

					// TODO get reader from writer instead? reader=writer?
					// TODO maybe the ArrayIO is versioned and NOT the reader/writers themselves.

					ReadTable readTable = TableUtils.create(store);

					// return some wrapped BufferedDataTable providing access to data, e.g. through
					// ReadTable table = TableUtils.create(reader, null);
					// TODO respect filters etc.
					return null;
				}
			};

			return null;
		}

		private PrimitiveType<?, ?>[] translate(DataTableSpec spec) {
			return null;
		}
	}

	/**
	 * Execution Context point of view
	 */

	interface BufferedDataTable {
		/* TODO whatever is our API here later with iteratorUnsafe etc */
		ReadTable getReadTable();
	}

	interface DataTableSpec {

	}

	interface DataColumnSpec {

	}

	interface TableContainer {
		BufferedDataTable close() throws Exception;
	}
}
