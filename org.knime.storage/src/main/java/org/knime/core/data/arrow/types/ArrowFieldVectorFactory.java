package org.knime.core.data.arrow.types;

public interface ArrowFieldVectorFactory<T> {
	T create();
}
