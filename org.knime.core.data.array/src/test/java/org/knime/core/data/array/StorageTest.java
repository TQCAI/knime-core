
package org.knime.core.data.array;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.PrimitiveType;
import org.knime.core.data.api.column.ColumnReadCursor;
import org.knime.core.data.api.column.ColumnWriteCursor;
import org.knime.core.data.api.column.access.ReadableStringAccess;
import org.knime.core.data.api.column.access.WritableStringAccess;
import org.knime.core.data.api.row.ReadableRowCursor;
import org.knime.core.data.api.row.ReadableRowTable;
import org.knime.core.data.api.row.WritableRow;
import org.knime.core.data.api.row.WritableRowTable;
import org.knime.core.data.array.ArrayColumnStoreFactory;
import org.knime.core.data.store.TableBackend;
import org.knime.core.data.store.TableUtils;

// TODO common test framework for all store based impl.
public class StorageTest {

	public static void main(final String[] args) throws Exception {
		final StorageTest storageTest = new StorageTest();
		for (int i = 0; i < 2; i++) {
			System.out.println("Iteration: " + i);
			storageTest.columnwiseWriteReadSingleColumnIdentityTest();
		}
	}

	/**
	 * Some variables
	 */
	// in numValues per vector
	public static final int BATCH_SIZE = 1_000_000;

	// num rows used for testing
	public static final long NUM_ROWS = 1_000_000;

	// some schema
	private static final ColumnType[] STRING_COLUMN = new ColumnType[] { new ColumnType() {

		@Override
		public String name() {
			return "My String Column";
		}

		@Override
		public PrimitiveType[] getPrimitiveTypes() {
			return new PrimitiveType[] { PrimitiveType.STRING };
		}
	} };

	@Test
	public void columnwiseWriteReadSingleColumnIdentityTest() throws Exception {
		// TODO: Store is not AutoCloseable any more - should it be?
		try (final TableBackend store = TableUtils.createTableStore(new ArrayColumnStoreFactory(BATCH_SIZE),
				STRING_COLUMN)) {

			// Create writable table on store. Just an access on store.
			final WriteTable writableTable = TableUtils.createWritableColumnTable(store);

			// first column write
			try (final ColumnWriteCursor<?> col0 = writableTable.getWritableColumn(0).access()) {
				final WritableStringAccess val0 = (WritableStringAccess) col0.get();
				for (long i = 0; i < NUM_ROWS; i++) {
					col0.fwd();
					val0.setStringValue("Entry" + i);
				}
			}

			// Done writing?
			final ReadTable readableTable = TableUtils.createReadableTable(store);

			// then read
			try (final ColumnReadCursor<?> col0 = readableTable.getReadableColumn(0).createReadableCursor()) {
				final ReadableStringAccess val0 = (ReadableStringAccess) col0.get();
				for (long i = 0; col0.canFwd(); i++) {
					col0.fwd();
					Assert.assertEquals("Entry" + i, val0.getStringValue());
				}
			}
		}
	}

	@Test
	public void rowwiseWriteReadSingleDoubleColumnIdentityTest() throws Exception {
		// Read/Write table...
		try (final TableBackend store = TableUtils.createTableStore(new ArrayColumnStoreFactory(BATCH_SIZE),
				STRING_COLUMN)) {

			// Create writable table on store. Just an access on store.
			final WritableRowTable writableTable = TableUtils.createWritableRowTable(store);

			try (final WritableRow row = writableTable.getWritableRow()) {
				final WritableStringAccess val0 = (WritableStringAccess) row.getWritableAccess(0);
				for (long i = 0; i < NUM_ROWS; i++) {
					row.fwd();
					val0.setStringValue("Entry " + i);
				}
			}

			// Done writing?
			final ReadableRowTable readableTable = TableUtils.createReadableRowTable(store);

			try (final ReadableRowCursor row = readableTable.getRowCursor()) {
				final ReadableStringAccess val0 = (ReadableStringAccess) row.getReadableAccess(0);
				for (long i = 0; row.canFwd(); i++) {
					row.fwd();
					Assert.assertEquals("Entry " + i, val0.getStringValue());
				}
			}
		}
	}
}