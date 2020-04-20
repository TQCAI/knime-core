package org.knime.core.data.arrow;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.arrow.vector.FieldVector;
import org.apache.arrow.vector.TypeLayout;
import org.apache.arrow.vector.VectorLoader;
import org.apache.arrow.vector.VectorSchemaRoot;
import org.apache.arrow.vector.ipc.ArrowFileWriter;
import org.apache.arrow.vector.ipc.message.ArrowFieldNode;
import org.apache.arrow.vector.ipc.message.ArrowRecordBatch;

import io.netty.buffer.ArrowBuf;

public class FieldVectorWriter<F extends FieldVector> implements AutoCloseable {

	private final File m_file;
	private ArrowFileWriter m_writer;
	private VectorLoader m_vectorLoader;
	private VectorSchemaRoot m_root;

	public FieldVectorWriter(final File file) throws IOException {
		m_file = file;
	}

	@SuppressWarnings("resource")
	public void flush(F vector) throws IOException {

		if (m_writer == null) {
			m_root = new VectorSchemaRoot(Collections.singletonList(vector.getField()),
					Collections.singletonList(vector));
			m_vectorLoader = new VectorLoader(m_root);
			m_writer = new ArrowFileWriter(m_root, null, new RandomAccessFile(m_file, "rw").getChannel());
		}

		// TODO there must be a better way?!
		final List<ArrowFieldNode> nodes = new ArrayList<>();
		final List<ArrowBuf> buffers = new ArrayList<>();
		appendNodes(vector, nodes, buffers);

		// Auto-closing makes sure that ArrowRecordBatch actually releases the buffers
		// again
		try (final ArrowRecordBatch batch = new ArrowRecordBatch((int) vector.getValueCount(), nodes, buffers)) {
			m_vectorLoader.load(batch);
			m_writer.writeBatch();
		}
	}

	@Override
	public void close() throws Exception {
		if (m_writer != null) {
			m_root.close();
			m_writer.close();
		}
	}

	// TODO: Copied from org.apache.arrow.vector.VectorUnloader. Is there a better
	// way to do all of this (including writing vectors in general)?
	private void appendNodes(final FieldVector vector, final List<ArrowFieldNode> nodes, final List<ArrowBuf> buffers) {
		nodes.add(new ArrowFieldNode(vector.getValueCount(), vector.getNullCount()));
		final List<ArrowBuf> fieldBuffers = vector.getFieldBuffers();
		final int expectedBufferCount = TypeLayout.getTypeBufferCount(vector.getField().getType());
		if (fieldBuffers.size() != expectedBufferCount) {
			throw new IllegalArgumentException(
					String.format("wrong number of buffers for field %s in vector %s. found: %s", vector.getField(),
							vector.getClass().getSimpleName(), fieldBuffers));
		}
		buffers.addAll(fieldBuffers);
		for (final FieldVector child : vector.getChildrenFromFields()) {
			appendNodes(child, nodes, buffers);
		}
	}

}