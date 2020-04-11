
package org.knime.core.data.api.access;

import org.knime.core.data.api.column.WritableAccess;

public interface WritableDoubleAccess extends WritableAccess {

	void setDoubleValue(double value);
}
