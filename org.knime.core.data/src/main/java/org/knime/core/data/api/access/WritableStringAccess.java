
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WriteAccess;

public interface WritableStringAccess extends WriteAccess {

	void setStringValue(String value);
}
