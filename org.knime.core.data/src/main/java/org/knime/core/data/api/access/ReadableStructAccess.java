package org.knime.core.data.api.access;

import org.knime.core.data.api.column.ReadableAccess;

public interface ReadableStructAccess extends ReadableAccess {
	ReadableAccess readableValueAt(long i);
}
