package org.knime.core.data.arrow;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowStreamReader;
import org.apache.arrow.vector.util.TransferPair;

/* NB: This reader has best performance when data is accessed sequentially row-wise.
* TODO Maybe different flush / loader combinations are configurable per node later?
*/
public class FieldVectorReader<V extends FieldVector> implements AutoCloseable {

	// some constants
	private final BufferAllocator m_alloc;

	// Varies with each partition
	private VectorSchemaRoot m_root;

	private File m_file;

	private ArrowStreamReader m_reader;

	// TODO support for column filtering and row filtering ('TableFilter'), i.e.
	// only load required columns / rows from disc. Rows should be easily possible
	// by using 'ArrowBlock'
	// TODO maybe easier with parquet backend?
	public FieldVectorReader(final File file, final BufferAllocator alloc) throws IOException {
		m_alloc = alloc;
		m_file = file;
	}

	// Assumption for this reader: sequential loading.
	@SuppressWarnings("resource")
	public V load(long index) throws IOException {
		if (m_reader == null) {
			m_reader = new ArrowStreamReader(new RandomAccessFile(m_file, "rw").getChannel(), m_alloc);
			m_root = m_reader.getVectorSchemaRoot();
		}

		// load next
		m_reader.loadNextBatch();

		// TODO Check if there is a faster way
		final FieldVector vector = m_root.getVector(0);
		final TransferPair transferPair = vector.getTransferPair(m_alloc);
		transferPair.transfer();
		@SuppressWarnings("unchecked")
		final V newVector = (V) transferPair.getTo();
		return (V) newVector;
	}

	@Override
	public void close() throws Exception {
		if (m_root != null) {
			m_reader.close();
			m_alloc.close();
		}
	}
}
