package org.knime.core.data.struct;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.StorageTest;
import org.knime.core.data.access.ReadableDoubleAccess;
import org.knime.core.data.access.ReadableStringAccess;
import org.knime.core.data.access.ReadableStructAccess;
import org.knime.core.data.access.WritableDoubleAccess;
import org.knime.core.data.access.WritableStringAccess;
import org.knime.core.data.access.WritableStructAccess;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.column.NativeColumnType;
import org.knime.core.data.column.ReadableCursor;
import org.knime.core.data.column.WritableCursor;
import org.knime.core.data.impl.arrow.ArrowUtils;
import org.knime.core.data.partition.ReadablePartitionedTable;
import org.knime.core.data.partition.Store;
import org.knime.core.data.partition.WritablePartitionedTable;
import org.knime.core.data.table.column.ColumnSchema;

public class StructTest {

	final static long NUM_ROWS = 100;

	// some funky struct schema
	private static final ColumnSchema[] STRUCT_SCHEMA = new ColumnSchema[] { new ColumnSchema() {
		@Override
		public String name() {
			return "My String, Double Struct";
		}

		@Override
		public ColumnType getColumnType() {
			return new ColumnType() {

				@Override
				public NativeColumnType[] getNativeTypes() {
					return new NativeColumnType[] { NativeColumnType.STRING, NativeColumnType.DOUBLE };
				}
			};
		}
	} };

	class Person {
		String name;
		long age;
	}

	@Test
	public void columnwiseWriteReadStructColumnIdentityTest() throws Exception {
		try (final Store root = ArrowUtils.createArrowStore(StorageTest.OFFHEAP_SIZE, StorageTest.BATCH_SIZE,
				STRUCT_SCHEMA)) {

			// Create writable table on store. Just an access on store.
			final WritablePartitionedTable writableTable = new WritablePartitionedTable(root);

			// first column write
			try (final WritableCursor col0 = writableTable.getWritableColumn(0).createWritableCursor()) {
				final WritableStructAccess val0 = (WritableStructAccess) col0.getValue();
				// TODO we could offer convenience API with reflection to operate on POJO
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
			final ReadablePartitionedTable readableTable = new ReadablePartitionedTable(root);

			// then read
			try (final ReadableCursor col0 = readableTable.getReadableColumn(0).createCursor()) {
				final ReadableStructAccess val0 = (ReadableStructAccess) col0.getValue();
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

}
