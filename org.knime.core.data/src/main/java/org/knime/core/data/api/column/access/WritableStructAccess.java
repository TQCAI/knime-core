package org.knime.core.data.api.column.access;

import org.knime.core.data.api.column.WritableAccess;

public interface WritableStructAccess extends WritableAccess {
	WritableAccess writableValueAt(long i);
}
