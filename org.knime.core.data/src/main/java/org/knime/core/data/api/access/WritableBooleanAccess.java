
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WriteAccess;

public interface WritableBooleanAccess extends WriteAccess {

	void setBooleanValue(boolean value);
}
