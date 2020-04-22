package org.knime.core.data.access;

public interface DoubleReadAccess extends NumericReadAccess {
	// NB: marker interface
	double getDouble();
}