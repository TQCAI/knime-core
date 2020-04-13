package org.knime.core.data.store;

import org.knime.core.data.api.column.domain.Domain;

public interface UpdatableDomain<T> extends Domain {
	void update(T data);
}
