package org.knime.core.data.store.chunk;

import java.util.Iterator;

import org.knime.core.data.store.vec.VecAccess;
import org.knime.core.data.store.vec.VecAccessible;
import org.knime.core.data.store.vec.VecAccessibleOnVecAccessibles;
import org.knime.core.data.store.vec.VecSchema;

public class ChunkedVecReadAccessible implements VecAccessible {

	private ChunkStore m_store;
	private VecAccessibleOnVecAccessibles m_vecAccessible;

	public ChunkedVecReadAccessible(final ChunkStore store) {
		m_store = store;

		// TODO maybe don't use iterator here is we everywhere else use "access"
		// ... (brr.later!)
		m_vecAccessible = new VecAccessibleOnVecAccessibles(store.schema(), new Iterable<VecAccessible>() {
			@Override
			public Iterator<VecAccessible> iterator() {
				return new Iterator<VecAccessible>() {

					private long m_chunkIdx = 0;

					@Override
					public boolean hasNext() {
						return m_chunkIdx < m_store.numChunks();
					}

					// Reusable vec-accessible!!!!
					@Override
					public VecAccessible next() {
						// TODO load chunk into my updateableVectorAccessible
//						new VecAccessible() {
//
//							@Override
//							public VecSchema schema() {
//								return m_store.schema();
//							}
//
//							@Override
//							public VecAccess access() {
//								m_store.load(m_chunkIdx++);
//								return ;
//							}
//						};

//						return proxy;
					}
				};
			}
		});
	}

	@Override
	public VecSchema schema() {
		return m_store.schema();
	}

	@Override
	public VecAccess access() {
		return m_vecAccessible.access();
	}

}
