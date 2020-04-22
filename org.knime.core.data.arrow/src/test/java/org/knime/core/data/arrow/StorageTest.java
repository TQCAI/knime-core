
package org.knime.core.data.arrow;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.knime.core.data.column.ColumnType;
import org.knime.core.data.record.RecordStore;
import org.knime.core.data.row.RowReadCursor;
import org.knime.core.data.row.RowReadTable;
import org.knime.core.data.row.RowTableUtils;
import org.knime.core.data.row.RowWriteCursor;
import org.knime.core.data.row.RowWriteTable;
import org.knime.core.data.value.DoubleReadValue;
import org.knime.core.data.value.DoubleWriteValue;

public class StorageTest {

	/**
	 * Some variables
	 */
	// in numValues per vector
	public static final int RECORDBATCH_SIZE = 4;

	// in bytes
	public static final long OFFHEAP_SIZE = 2000_000_000;

	// num rows used for testing
	public static final long NUM_ROWS = 6;

	// some schema
	private static final ColumnType<?, ?>[] SCHEMA = new ColumnType[] { ColumnType.DoubleType.INSTANCE };

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
	public void identityTest() throws Exception {

		// file
		final File f = Files.createTempFile("KNIME-" + UUID.randomUUID().toString(), ".knarrow").toFile();
		f.deleteOnExit();

		// TODO do we need both (format and store?)
		ArrowRecordFormat format = new ArrowRecordFormat(RECORDBATCH_SIZE);
		RecordStore store = format.create(SCHEMA, f, null);

		RowWriteTable writeTable = RowTableUtils.createRowWriteTable(format.createFactory(SCHEMA), store.getWriter());
		RowWriteCursor writeCursor = writeTable.cursor();
		DoubleWriteValue doubleWriteValue = (DoubleWriteValue) writeCursor.get(0);
		for (int i = 0; i < NUM_ROWS; i++) {
			writeCursor.fwd();
			if (i % 100 == 0) {
				doubleWriteValue.setMissing();
			}
			doubleWriteValue.setDouble(i);
		}

		RowReadTable readTable = RowTableUtils.createRowReadTable(SCHEMA, store, null);
		RowReadCursor readCursor = readTable.newCursor();
		DoubleReadValue doubleReadValue = (DoubleReadValue) readCursor.get(0);
		for (int i = 0; i < NUM_ROWS; i++) {
			readCursor.fwd();
			if (i % 100 == 0) {
				Assert.assertTrue(doubleReadValue.isMissing());
			}
			assertEquals(i, doubleReadValue.getDouble(), 0.00000000000000001);
		}
	}
}
