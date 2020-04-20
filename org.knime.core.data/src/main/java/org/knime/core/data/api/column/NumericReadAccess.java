
package org.knime.core.data.api.column;

/**
 * Base interface for proxies through which data values are read.
 * 
 * TODO do we need that?
 */
public interface NumericReadAccess extends ReadAccess {
	double getDouble();
}
