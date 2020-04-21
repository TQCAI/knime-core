package org.knime.core.data.store;

import org.knime.core.data.DataReader;

public interface DataReadStore<D, C extends DataReaderConfig> {
	DataReader<D> createReader(C config);
}
