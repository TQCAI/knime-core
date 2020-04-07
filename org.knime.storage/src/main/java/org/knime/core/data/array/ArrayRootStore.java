package org.knime.core.data.array;

import java.io.IOException;

import org.knime.core.data.array.types.DoubleArray;
import org.knime.core.data.array.types.DoubleArrayFactory;
import org.knime.core.data.array.types.StringArray;
import org.knime.core.data.array.types.StringArrayFactory;
import org.knime.core.data.cache.SequentialCache;
import org.knime.core.data.partition.PartitionStore;
import org.knime.core.data.partition.Store;
import org.knime.core.data.table.column.ColumnSchema;
import org.knime.core.data.table.column.NativeType;

// TODO persistence done with one file per column at the moment. That's not working out will in case of very wide-data with only little rows. Also problematic as so many small files are created.
// TODO likely better approach: use parquet as persistence layer. can be done on implementation level without API changes.
class ArrayRootStore implements Store {
	
	private final ArrayPartitionStore<?>[] m_stores;

	// TODO maybe we can have something like an adaptive batchSize at some point
	// TODO documentation that vectorCapacity will be adopted.
	public ArrayRootStore(final int batchSize, final ColumnSchema... schemas) {
		try {
			m_stores = new ArrayPartitionStore[schemas.length];

			// TODO check if there are smarter ways to do this in arrow than that
			for (int i = 0; i < schemas.length; i++) {
				final NativeType[] nativeTypes = schemas[i].getColumnType().getNativeTypes();
				if (nativeTypes.length == 1) {
					m_stores[i] = create(nativeTypes[0], batchSize);
				} else {
					throw new UnsupportedOperationException("Not yet implemented.");
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private DefaultArrayPartitionStore<?> create(NativeType type, int capacity) throws IOException {
		// TODO make sure that vectorCapacity is toThePowerOf2

		// TODO no idea what a good init size or max-size is. Actually it should
		// type-dependent
		switch (type) {
		// TODO a bit clunky. Util methods?
		// TODO last argument for DefaultPartitionStore (batchSize) could also be
		// retrieved from VectorFactory.
		case DOUBLE:
			return new DefaultArrayPartitionStore<>(() -> new DoubleArray.NativeDoubleValue(),
					() -> new DoubleArrayFactory(capacity).create(), new SequentialCache<>(null, null));
		case STRING:
			return new DefaultArrayPartitionStore<>(() -> new StringArray.NativeStringValue(),
					() -> new StringArrayFactory(capacity).create(), new SequentialCache<>(null, null));
		default:
			throw new IllegalArgumentException("Unknown or not yet implemented NativeType " + type);
		}
	}

	@Override
	public long getNumStores() {
		return m_stores.length;
	}

	@Override
	public PartitionStore<?> getStoreAt(long index) {
		return m_stores[(int) index];
	}

	/* TODO define what flush means ('write to disc' and? 'release memory') */
	@Override
	public void flush() throws Exception {
		for (int i = 0; i < m_stores.length; i++) {
			m_stores[i].flush();
		}
	}

	@Override
	public void close() throws Exception {
		// release memory of stores
		for (int i = 0; i < m_stores.length; i++) {
			m_stores[i].close();
		}
	}
}
