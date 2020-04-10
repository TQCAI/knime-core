
package org.knime.core.data.access;

import org.knime.core.data.column.WritableAccess;

public interface WritableBooleanAccess extends WritableAccess {

	void setBooleanValue(boolean value);
}
