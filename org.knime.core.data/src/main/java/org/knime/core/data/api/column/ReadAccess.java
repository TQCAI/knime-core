
package org.knime.core.data.api.column;

/**
 * Base interface for proxies through which data values are read.
 */
public interface ReadAccess {

	boolean isMissing();
}
