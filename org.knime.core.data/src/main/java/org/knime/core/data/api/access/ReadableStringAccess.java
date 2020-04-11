
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.ReadableAccess;

public interface ReadableStringAccess extends ReadableAccess {

	String getStringValue();
}
