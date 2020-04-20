package org.knime.core.data.data;

public interface DataFactory<D extends Data> {
	D create();
}
