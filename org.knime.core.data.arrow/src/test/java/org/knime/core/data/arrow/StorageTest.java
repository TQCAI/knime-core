
package org.knime.core.data.arrow;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.api.ReadableTable;
import org.knime.core.data.api.WritableTable;
import org.knime.core.data.api.access.ReadableStringAccess;
import org.knime.core.data.api.access.WritableStringAccess;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.NativeColumnType;
import org.knime.core.data.api.column.ReadableCursor;
import org.knime.core.data.api.column.WritableCursor;
import org.knime.core.data.api.row.ReadableRowCursor;
import org.knime.core.data.api.row.ReadableRowTable;
import org.knime.core.data.api.row.WritableRow;
import org.knime.core.data.api.row.WritableRowTable;
import org.knime.core.data.store.TableStore;
import org.knime.core.data.store.TableStoreUtils;

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
		public NativeColumnType[] getNativeTypes() {
			return new NativeColumnType[] { NativeColumnType.STRING };
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
		try (final TableStore store = TableStoreUtils.createTableStore(new ArrowStoreFactory(BATCH_SIZE, OFFHEAP_SIZE),
				STRING_COLUMN)) {

			// Create writable table on store. Just an access on store.
			final WritableTable writableTable = TableStoreUtils.createWritableColumnTable(store);

			// first column write
			try (final WritableCursor<?> col0 = writableTable.getWritableColumn(0).createWritableCursor()) {
				final WritableStringAccess val0 = (WritableStringAccess) col0.get();
				for (long i = 0; i < NUM_ROWS; i++) {
					col0.fwd();
					val0.setStringValue("Entry" + i);
				}
			}

			// Done writing?
			final ReadableTable readableTable = TableStoreUtils.createReadableTable(store);

			// then read
			try (final ReadableCursor<?> col0 = readableTable.getReadableColumn(0).createReadableCursor()) {
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
		try (final TableStore store = TableStoreUtils.createTableStore(new ArrowStoreFactory(BATCH_SIZE, OFFHEAP_SIZE),
				STRING_COLUMN)) {

			// Create writable table on store. Just an access on store.
			final WritableRowTable writableTable = TableStoreUtils.createWritableRowTable(store);

			try (final WritableRow row = writableTable.getWritableRow()) {
				final WritableStringAccess val0 = (WritableStringAccess) row.getWritableAccess(0);
				for (long i = 0; i < NUM_ROWS; i++) {
					row.fwd();
					val0.setStringValue("Entry " + i);
				}
			}

			final ReadableRowTable readableTable = TableStoreUtils.createReadableRowTable(store);

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
