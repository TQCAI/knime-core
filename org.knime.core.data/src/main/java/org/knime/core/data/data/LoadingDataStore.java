package org.knime.core.data.data;

public interface LoadingDataStore extends DataStore {
	<D extends Data> DataLoader<D> createLoader(long columnIndex);
}
