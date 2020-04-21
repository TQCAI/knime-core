package org.knime.core.data.api.domain;

import org.knime.core.data.data.Data;

public interface MutableDomain<D extends Data> extends Domain {
	void update(D data);
}
