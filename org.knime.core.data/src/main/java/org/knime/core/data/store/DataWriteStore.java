package org.knime.core.data.store;

import org.knime.core.data.DataWriter;

public interface DataWriteStore<D> {
	DataWriter<D> getWriter();
}
