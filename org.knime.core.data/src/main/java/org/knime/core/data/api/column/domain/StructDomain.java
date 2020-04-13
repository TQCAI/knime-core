package org.knime.core.data.api.column.domain;

public interface StructDomain extends Domain {
	Domain getDomainAt(long index);
}
