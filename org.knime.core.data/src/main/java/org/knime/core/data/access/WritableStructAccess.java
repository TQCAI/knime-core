package org.knime.core.data.access;

import org.knime.core.data.column.WritableAccess;

public interface WritableStructAccess extends WritableAccess {
	WritableAccess writableValueAt(long i);
}
