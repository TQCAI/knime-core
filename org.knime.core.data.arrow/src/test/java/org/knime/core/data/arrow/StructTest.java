
package org.knime.core.data.arrow;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.api.ReadableTable;
import org.knime.core.data.api.WritableTable;
import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.ReadableStringAccess;
import org.knime.core.data.api.access.ReadableStructAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.api.access.WritableStringAccess;
import org.knime.core.data.api.access.WritableStructAccess;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.NativeColumnType;
import org.knime.core.data.api.column.ReadableCursor;
import org.knime.core.data.api.column.WritableCursor;
import org.knime.core.data.arrow.ArrowStoreFactory;
import org.knime.core.data.store.TableStore;
import org.knime.core.data.store.TableStoreUtils;

public class StructTest {

	final static long NUM_ROWS = 100;

	// some funky struct schema
	private static final ColumnType[] STRUCT_COLUMN = new ColumnType[] { new ColumnType() {

		@Override
		public String name() {
			return "My String, Double Struct";
		}

		@Override
		public NativeColumnType[] getNativeTypes() {
			return new NativeColumnType[] { NativeColumnType.STRING, NativeColumnType.DOUBLE };
		}
	} };

	class Person {

		String name;
		long age;
	}

	@Test
	public void columnwiseWriteReadStructColumnIdentityTest() throws Exception {
		final TableStore store = TableStoreUtils.createTableStore(new ArrowStoreFactory(StorageTest.BATCH_SIZE,
			StorageTest.OFFHEAP_SIZE), STRUCT_COLUMN);

		// Create writable table on store. Just an access on store.
		final WritableTable writableTable = TableStoreUtils.createWritableColumnTable(store);

		// first column write
		try (final WritableCursor<?> col0 = writableTable.getWritableColumn(0).createWritableCursor()) {
			final WritableStructAccess val0 = (WritableStructAccess) col0.get();
			// TODO we could offer convenience API with reflection to operate on
			// POJO
			// structs :-)
			final WritableStringAccess stringValue = (WritableStringAccess) val0.writableValueAt(0);
			final WritableDoubleAccess doubleValue = (WritableDoubleAccess) val0.writableValueAt(1);
			for (long i = 0; i < NUM_ROWS; i++) {
				col0.fwd();
				stringValue.setStringValue("Name " + i);
				doubleValue.setDoubleValue(i);
			}
		}

		// Done writing?
		final ReadableTable readableTable = TableStoreUtils.createReadableTable(store);

		// then read
		try (final ReadableCursor<?> col0 = readableTable.getReadableColumn(0).createReadableCursor()) {
			final ReadableStructAccess val0 = (ReadableStructAccess) col0.get();
			final ReadableStringAccess stringValue = (ReadableStringAccess) val0.readableValueAt(0);
			final ReadableDoubleAccess doubleValue = (ReadableDoubleAccess) val0.readableValueAt(1);
			for (long i = 0; col0.canFwd(); i++) {
				col0.fwd();
				Assert.assertEquals(i, doubleValue.getDoubleValue(), 0.0000001);
				Assert.assertEquals("Name " + i, stringValue.getStringValue());
			}
		}
	}
}
