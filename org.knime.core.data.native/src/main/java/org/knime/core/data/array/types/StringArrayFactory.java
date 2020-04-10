
package org.knime.core.data.array.types;

public final class StringArrayFactory extends AbstractNativeArrayFactory<StringArray> {

	public StringArrayFactory(final int partitionCapacity) {
		super(partitionCapacity);
	}

	@Override
	StringArray create(final int capacity) {
		return new StringArray(capacity);
	}
}
