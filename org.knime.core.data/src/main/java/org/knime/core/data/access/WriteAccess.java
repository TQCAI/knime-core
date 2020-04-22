
package org.knime.core.data.access;

/**
 * Base interface for proxies through which data values are written.
 */
public interface WriteAccess {

	// TODO what is the default?
	void setMissing();
}
