package org.knime.core.data.array.io;

import org.knime.core.data.array.Array;
import org.knime.core.data.array.ArrayReadStore;
import org.knime.core.data.array.ArrayWriteStore;

public interface ArrayIO<A extends Array> extends ArrayReadStore<A>, ArrayWriteStore<A> {
	// NB: Marker interface
}