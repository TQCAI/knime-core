
package org.knime.core.data.arrow;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.WriteRowTable;
import org.knime.core.data.access.ReadableStringAccess;
import org.knime.core.data.access.WritableStringAccess;
import org.knime.core.data.arrow.old.ArrowStoreFactory;
import org.knime.core.data.column.ColumnReadCursor;
import org.knime.core.data.column.ColumnReadableTable;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.ColumnWriteTable;
import org.knime.core.data.column.ColumnWriteCursor;
import org.knime.core.data.row.RowReadCursor;
import org.knime.core.data.row.RowReadTable;
import org.knime.core.data.row.RowWriteCursor;
import org.knime.core.data.store.TableBackend;
import org.knime.core.data.store.TableUtils;

public class StorageTest {

	public static void main(final String[] args) throws Exception {
		final StorageTest storageTest = new StorageTest();
		for (int i = 0; i < 2; i++) {
			storageTest.columnwiseWriteReadSingleColumnIdentityTest();
		}
	}

	/**
	 * Some variables
	 */
	// in numValues per vector
	public static final int BATCH_SIZE = 4;

	// in bytes
	public static final long OFFHEAP_SIZE = 2000_000_000;

	// num rows used for testing
	public static final long NUM_ROWS = 6;

	// some schema
	private static final ColumnType[] STRING_COLUMN = new ColumnType[] { new ColumnType() {

		@Override
		public String name() {
			return "My String Column";
		}

		@Override
		public ColumnType[] getPrimitiveTypes() {
			return new ColumnType[] { ColumnType.STRING };
		}
	} };

	/**
	 * TESTS
	 */

	// @Test
	public void doubleArrayTest() {
		final double[] array = new double[100_000_000];
		for (int i = 0; i < array.length; i++) {
			array[i] = i;
		}

		for (int i = 0; i < array.length; i++) {
			final double k = array[i];
			Assert.assertEquals(array[i], k, 0.00000000000001);
		}
	}

	@Test
	public void columnwiseWriteReadSingleColumnIdentityTest() throws Exception {
		try (final TableBackend store = TableUtils.cache(
				TableUtils.createTableStore(new ArrowStoreFactory(BATCH_SIZE, OFFHEAP_SIZE), STRING_COLUMN))) {

			// Create writable table on store. Just an access on store.
			final ColumnWriteTable writableTable = TableUtils.createWritableColumnTable(store);

			// first column write
			try (final ColumnWriteCursor<?> col0 = writableTable.getWriteColumn(0).access()) {
				final WritableStringAccess val0 = (WritableStringAccess) col0.get();
				for (long i = 0; i < NUM_ROWS; i++) {
					col0.fwd();
					val0.setStringValue("Entry" + i);
				}
			}

			// Done writing?
			final ColumnReadableTable readableTable = TableUtils.createReadableTable(store);

			// then read
			try (final ColumnReadCursor<?> col0 = readableTable.getReadColumn(0).createReadableCursor()) {
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
		try (final TableBackend store = TableUtils.createTableStore(new ArrowStoreFactory(BATCH_SIZE, OFFHEAP_SIZE),
				STRING_COLUMN)) {

			// Create writable table on store. Just an access on store.
			final WriteRowTable writableTable = TableUtils.createWritableRowTable(store);

			try (final RowWriteCursor row = writableTable.getWritableRow()) {
				final WritableStringAccess val0 = (WritableStringAccess) row.getWriteAccess(0);
				for (long i = 0; i < NUM_ROWS; i++) {
					row.fwd();
					val0.setStringValue("Entry " + i);
				}
			}

			final RowReadTable readableTable = TableUtils.createReadableRowTable(store);

			try (final RowReadCursor row = readableTable.getRowCursor()) {
				final ReadableStringAccess val0 = (ReadableStringAccess) row.getReadableAccess(0);
				for (long i = 0; row.canFwd(); i++) {
					row.fwd();
					Assert.assertEquals("Entry " + i, val0.getStringValue());
				}
			}
		}
	}
}
