
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.ReadAccess;

public interface ReadableStringAccess extends ReadAccess {

	String getStringValue();
}
