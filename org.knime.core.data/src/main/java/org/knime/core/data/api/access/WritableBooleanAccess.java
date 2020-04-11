
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WritableAccess;

public interface WritableBooleanAccess extends WritableAccess {

	void setBooleanValue(boolean value);
}
