
package org.knime.core.data.array.types;

public class DoubleArrayFactory extends AbstractNativeArrayFactory<DoubleArray> {

	public DoubleArrayFactory(final int partitionCapacity) {
		super(partitionCapacity);
	}

	@Override
	DoubleArray create(final int capacity) {
		return new DoubleArray(capacity);
	}
}
