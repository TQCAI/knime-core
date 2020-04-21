package org.knime.core.data.api.access;

import org.knime.core.data.api.column.ReadAccess;

public interface ReadableStructAccess extends ReadAccess {
	ReadAccess readableValueAt(long i);
}
