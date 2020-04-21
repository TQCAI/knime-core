
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.ReadAccess;

public interface ReadableBooleanAccess extends ReadAccess {

	boolean getBooleanValue();
}
