package org.knime.core.data.api.domain;

public interface Domain {

	long getNumMissing();

	long getNumNonMissing();

	default long getNumValues() {
		return getNumMissing() + getNumNonMissing();
	}

}
