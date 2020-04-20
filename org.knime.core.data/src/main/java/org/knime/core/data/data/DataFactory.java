package org.knime.core.data.data;

@FunctionalInterface
public interface DataFactory<D extends Data> {
	D create();
}
