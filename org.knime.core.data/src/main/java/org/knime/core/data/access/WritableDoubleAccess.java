
package org.knime.core.data.access;

import org.knime.core.data.column.WritableAccess;

public interface WritableDoubleAccess extends WritableAccess {

	void setDoubleValue(double value);
}
