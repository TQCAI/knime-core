
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WritableAccess;

public interface WritableStringAccess extends WritableAccess {

	void setStringValue(String value);
}
