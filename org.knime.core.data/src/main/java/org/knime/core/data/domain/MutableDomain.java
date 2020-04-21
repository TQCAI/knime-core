package org.knime.core.data.domain;

import org.knime.core.data.Data;

public interface MutableDomain<D extends Data> extends Domain {
	void update(D data);
}
