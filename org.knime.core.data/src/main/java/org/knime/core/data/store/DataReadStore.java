package org.knime.core.data.store;

import org.knime.core.data.Data;
import org.knime.core.data.DataReader;

public interface DataReadStore<D extends Data> {
	DataReader<D> createReader();
}
