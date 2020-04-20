
package org.knime.core.data.api.column;

/**
 * Base interface for proxies through which data values are written.
 */
public interface WriteAccess {

	void setMissing();
}
