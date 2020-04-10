package org.knime.core.data.impl.arrow.types;

public interface ArrowFieldVectorFactory<T> {
	T create();
}
