
package org.knime.core.data.access;

import org.knime.core.data.column.WritableAccess;

public interface WritableStringAccess extends WritableAccess {

	void setStringValue(String value);
}
