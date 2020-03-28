package org.knime.core.data.store.arrow;

import org.knime.core.data.store.Chunk;
import org.knime.core.data.store.ChunkSchema;
import org.knime.core.data.store.vec.Vec;
import org.knime.core.data.store.vec.VecReadAccess;
import org.knime.core.data.store.vec.VecWriteAccess;

public class ArrowInMemoryBatch implements Chunk {

	private final ChunkSchema m_spec;
	private final Vec[] m_accessibles;

	public ArrowInMemoryBatch(final ArrowVecFactory fac, ChunkSchema spec) {
		m_spec = spec;
		m_accessibles = new Vec[spec.getNumVecs()];
		for (int colIdx = 0; colIdx < m_accessibles.length; colIdx++) {
			m_accessibles[colIdx] = fac.create(spec.getVecTypeAt(colIdx));
		}
	}

	// close means: killed.
	@Override
	public void close() throws Exception {
		// TODO do we have to close writers if we also close schema root?
		for (int i = 0; i < m_accessibles.length; i++) {
			m_accessibles[i].close();
		}
	}

	@Override
	public ChunkSchema getSchema() {
		return m_spec;
	}

	@Override
	public VecReadAccess getReadAccessAt(int idx) {
		return m_accessibles[idx].readAccess();
	}

	@Override
	public VecWriteAccess getWriteAccessAt(int idx) {
		return m_accessibles[idx].writeAccess();
	}
}
