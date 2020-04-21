package org.knime.core.data;

@FunctionalInterface
public interface DataFactory<D extends Data> {
	D create();
}
