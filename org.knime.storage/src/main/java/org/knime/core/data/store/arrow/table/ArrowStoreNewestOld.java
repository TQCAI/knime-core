
package org.knime.core.data.store.arrow.table;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.memory.RootAllocator;
import org.apache.arrow.vector.BitVector;
import org.apache.arrow.vector.Float8Vector;
import org.apache.arrow.vector.VarCharVector;
import org.knime.core.data.store.arrow.table.value.ArrowReadableBooleanValueAccess;
import org.knime.core.data.store.arrow.table.value.ArrowReadableDoubleValueAccess;
import org.knime.core.data.store.arrow.table.value.ArrowReadableStringValueAccess;
import org.knime.core.data.store.arrow.table.value.ArrowWritableBooleanValueAccess;
import org.knime.core.data.store.arrow.table.value.ArrowWritableDoubleValueAccess;
import org.knime.core.data.store.arrow.table.value.ArrowWritableStringValueAccess;
import org.knime.core.data.store.partition.BufferReadableColumn;
import org.knime.core.data.store.partition.ColumnPartitionReadableValueAccess;
import org.knime.core.data.store.partition.ColumnPartitionStore;
import org.knime.core.data.store.partition.ColumnPartitionWritableValueAccess;
import org.knime.core.data.store.partition.DefaultPartitionedColumn;
import org.knime.core.data.store.partition.SingleColumnPartitionStore;
import org.knime.core.data.store.partition.WritableBufferColumn;
import org.knime.core.data.store.table.column.ColumnSchema;
import org.knime.core.data.store.table.column.ColumnType;
import org.knime.core.data.store.table.column.ReadableColumn;
import org.knime.core.data.store.table.column.Store;
import org.knime.core.data.store.table.column.WritableColumn;

public class ArrowStoreNewestOld implements Store {

	// Batch size in number of values, not in bytes!
	// TODO: Maybe adaptive later? Is the current value a reasonable default?
	private static final int BATCH_SIZE = 1024;

	private final File m_baseDirectory;

	private final BufferAllocator m_allocator;

	private final SingleColumnPartitionStore<?>[] m_vectorStores;

	private final WritableColumn[] m_writableLogicalColumns;

	private final ReadableColumn[] m_readableLogicalColumns;

	// TODO: Handle wide tables more efficiently.
	// - Instantiate vector stores and columns on demand
	// - Access column schema schema on demand
	public ArrowStoreNewestOld(final ColumnSchema[] schemas) throws IOException {
		// TODO: Test directory for now.
		m_baseDirectory = Files.createTempDirectory("knime-new-table-api-test").toFile();
		m_baseDirectory.deleteOnExit();

		m_vectorStores = new SingleColumnPartitionStore<?>[schemas.length];
		m_writableLogicalColumns = new WritableColumn[schemas.length];
		m_readableLogicalColumns = new ReadableColumn[schemas.length];

		// TODO: Make creating store & columns more concise. Factory?
		for (int i = 0; i < schemas.length; i++) {
			final ColumnType type = schemas[i].getType();
			switch (type) {
			case BOOLEAN: {
				final SingleColumnPartitionStore<BitVector> vectorStore = new DefaultPartitionedColumn<BitVector>(
						m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {

					@Override
					protected BitVector createNewChunk() {
						final BitVector vector = new BitVector((String) null, m_allocator);
						vector.allocateNew(BATCH_SIZE);
						return vector;
					}
				};
				m_vectorStores[i] = vectorStore;
				m_writableLogicalColumns[i] = //
						new WritablePartitionedColumn<>(new ArrowWritableBooleanValueAccess(), vectorStore);
				m_readableLogicalColumns[i] = //
						new BufferReadableColumn<>(ArrowReadableBooleanValueAccess::new, vectorStore);
				break;
			}
			case DOUBLE: {
				final SingleColumnPartitionStore<Float8Vector> vectorStore = new DefaultPartitionedColumn<Float8Vector>(
						m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {

					@Override
					protected Float8Vector createNewChunk() {
						final Float8Vector vector = new Float8Vector((String) null, m_allocator);
						vector.allocateNew(BATCH_SIZE);
						return vector;
					}
				};
				m_vectorStores[i] = vectorStore;
				m_writableLogicalColumns[i] = //
						new WritablePartitionedColumn<>(new ArrowWritableDoubleValueAccess(), vectorStore);
				m_readableLogicalColumns[i] = //
						new BufferReadableColumn<>(ArrowReadableDoubleValueAccess::new, vectorStore);
				break;
			}
			case STRING: {
				final SingleColumnPartitionStore<VarCharVector> vectorStore = new DefaultPartitionedColumn<VarCharVector>(
						m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {

					@Override
					protected VarCharVector createNewChunk() {
						final VarCharVector vector = new VarCharVector((String) null, m_allocator);
						// TODO: This assumes that string values are of size 64 bytes on
						// average. This might need tweaking or should at least be
						// configurable somewhere.
						vector.allocateNew(64l * BATCH_SIZE, BATCH_SIZE);
						return vector;
					}
				};
				m_vectorStores[i] = vectorStore;
				m_writableLogicalColumns[i] = //
						new WritablePartitionedColumn<>(new ArrowWritableStringValueAccess(), vectorStore);
				m_readableLogicalColumns[i] = //
						new BufferReadableColumn<>(ArrowReadableStringValueAccess::new, vectorStore);
				break;
			}
			default:
				// TODO: Support all possible types.
				throw new IllegalStateException("Type: " + type + " is not supported.");
			}
		}
	}

	@Override
	public long getNumColumns() {
		return m_writableLogicalColumns.length;
	}

	private ColumnPartitionStore getOrCreate(long index) {
		if (m_stores[(int) index] == null) {
			ColumnType type = m_schema[(int) index].getType();
			switch (type) {
			case BOOLEAN:
				break;
			case DOUBLE:
				break;
			case STRING:
				break;
			default:
				throw new IllegalStateException("Type: " + type + " is not supported.");
			}
		}

		return m_stores[(int) index];
	}

	private ColumnPartitionReadableValueAccess createReadAccess(long index) {
		ColumnType type = m_schema[(int) index].getType();
		switch (type) {
		case BOOLEAN:
			break;
		case DOUBLE:
			break;
		case STRING:
			break;
		default:
			throw new IllegalStateException("Type: " + type + " is not supported.");
		}
	}

	private ColumnPartitionWritableValueAccess createWriteAccess(long index) {
		ColumnType type = m_schema[(int) index].getType();
		switch (type) {
		case BOOLEAN:
			break;
		case DOUBLE:
			break;
		case STRING:
			break;
		default:
			throw new IllegalStateException("Type: " + type + " is not supported.");
		}
	}

//
//	ColumnPartitionStore createColumnPartitionStore(ColumnSchema schema) {
//		// TODO: Make creating store & columns more concise. Factory?
//		final ColumnType type = schema.getType();
//		switch (type) {
//		case BOOLEAN: {
//			final ArrowColumnPartitionStore<BitVector> vectorStore = new ArrowColumnPartitionStore<BitVector>(
//					m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {
//
//				@Override
//				protected BitVector createNewChunk() {
//					final BitVector vector = new BitVector((String) null, m_allocator);
//					vector.allocateNew(BATCH_SIZE);
//					return vector;
//				}
//			};
//			m_vectorStores[i] = vectorStore;
//			m_writableLogicalColumns[i] = //
//					new WritablePartitionedColumn<>(new ArrowWritableBooleanValueAccess(), vectorStore);
//			m_readableLogicalColumns[i] = //
//					new BufferReadableColumn<>(ArrowReadableBooleanValueAccess::new, vectorStore);
//			break;
//		}
//		case DOUBLE: {
//			final SingleColumnPartitionStore<Float8Vector> vectorStore = new DefaultPartitionedColumn<Float8Vector>(
//					m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {
//
//				@Override
//				protected Float8Vector createNewChunk() {
//					final Float8Vector vector = new Float8Vector((String) null, m_allocator);
//					vector.allocateNew(BATCH_SIZE);
//					return vector;
//				}
//			};
//			m_vectorStores[i] = vectorStore;
//			m_writableLogicalColumns[i] = //
//					new WritablePartitionedColumn<>(new ArrowWritableDoubleValueAccess(), vectorStore);
//			m_readableLogicalColumns[i] = //
//					new BufferReadableColumn<>(ArrowReadableDoubleValueAccess::new, vectorStore);
//			break;
//		}
//		case STRING: {
//			final SingleColumnPartitionStore<VarCharVector> vectorStore = new DefaultPartitionedColumn<VarCharVector>(
//					m_baseDirectory, i, /* TODO */ null, /* TODO */ m_allocator) {
//
//				@Override
//				protected VarCharVector createNewChunk() {
//					final VarCharVector vector = new VarCharVector((String) null, m_allocator);
//					// TODO: This assumes that string values are of size 64 bytes on
//					// average. This might need tweaking or should at least be
//					// configurable somewhere.
//					vector.allocateNew(64l * BATCH_SIZE, BATCH_SIZE);
//					return vector;
//				}
//			};
//			m_vectorStores[i] = vectorStore;
//			m_writableLogicalColumns[i] = //
//					new WritablePartitionedColumn<>(new ArrowWritableStringValueAccess(), vectorStore);
//			m_readableLogicalColumns[i] = //
//					new BufferReadableColumn<>(ArrowReadableStringValueAccess::new, vectorStore);
//			break;
//		}
//		default:
//			// TODO: Support all possible types.
//			throw new IllegalStateException("Type: " + type + " is not supported.");
//		}
//	}
}
