
package org.knime.core.data.arrow;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.api.NativeType;
import org.knime.core.data.api.ReadTable;
import org.knime.core.data.api.WriteTable;
import org.knime.core.data.api.access.ReadableDoubleAccess;
import org.knime.core.data.api.access.ReadableStructAccess;
import org.knime.core.data.api.access.WritableDoubleAccess;
import org.knime.core.data.api.access.WritableStructAccess;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.ColumnReadCursor;
import org.knime.core.data.api.column.ColumnWriteCursor;
import org.knime.core.data.api.column.access.ReadableStringAccess;
import org.knime.core.data.api.column.access.WritableStringAccess;
import org.knime.core.data.arrow.old.ArrowStoreFactory;
import org.knime.core.data.store.TableBackend;
import org.knime.core.data.store.TableUtils;

public class StructTest {

	final static long NUM_ROWS = 100;

	// some funky struct schema
	private static final ColumnType[] STRUCT_COLUMN = new ColumnType[] { new ColumnType() {

		@Override
		public String name() {
			return "My String, Double Struct";
		}

		@Override
		public NativeType[] getPrimitiveTypes() {
			return new NativeType[] { NativeType.STRING, NativeType.DOUBLE };
		}
	} };

	class Person {

		String name;
		long age;
	}

	@Test
	public void columnwiseWriteReadStructColumnIdentityTest() throws Exception {
		final TableBackend store = TableUtils.createTableStore(new ArrowStoreFactory(StorageTest.BATCH_SIZE,
			StorageTest.OFFHEAP_SIZE), STRUCT_COLUMN);

		// Create writable table on store. Just an access on store.
		final WriteTable writableTable = TableUtils.createWritableColumnTable(store);

		// first column write
		try (final ColumnWriteCursor<?> col0 = writableTable.getWriteColumn(0).access()) {
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
		final ReadTable readableTable = TableUtils.createReadableTable(store);

		// then read
		try (final ColumnReadCursor<?> col0 = readableTable.getReadColumn(0).createReadableCursor()) {
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
