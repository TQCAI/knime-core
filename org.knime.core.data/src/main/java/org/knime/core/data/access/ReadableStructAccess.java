package org.knime.core.data.access;

import org.knime.core.data.column.ReadableAccess;

public interface ReadableStructAccess extends ReadableAccess {
	ReadableAccess readableValueAt(long i);
}
