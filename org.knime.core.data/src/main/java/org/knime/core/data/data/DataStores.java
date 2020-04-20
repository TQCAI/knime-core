package org.knime.core.data.data;

import org.knime.core.data.api.PrimitiveType;

public class DataStores {

	public static ConsumingDataStore addAdapter(ConsumingDataStore store, ConsumingDataStoreAdapter adapter) {
		return new AdaptedConsumingDataStore(store, adapter);
	}

	private static final class AdaptedConsumingDataStore implements ConsumingDataStore {
		private final ConsumingDataStore m_store;
		private final ConsumingDataStoreAdapter m_adapterStore;

		private AdaptedConsumingDataStore(ConsumingDataStore store, ConsumingDataStoreAdapter adapterStore) {
			// TODO instanceof check on ConsumingDataStore. In case it is already an
			// adapter, we can unpack and re-adapt.Saves method calls...

			this.m_store = store;
			this.m_adapterStore = adapterStore;
		}

		@Override
		public PrimitiveType<?, ?>[] getPrimitiveSpec() {
			return m_store.getPrimitiveSpec();
		}

		@Override
		public void close() throws Exception {
			m_store.close();
		}

		@Override
		public <D extends Data> DataConsumer<D> getConsumer(long columnIndex) {
			final DataConsumer<D> consumer = m_store.getConsumer(columnIndex);
			if (m_adapterStore.hasAdapter(columnIndex)) {
				return new DataConsumer<D>() {

					final DataConsumer<D> m_adapter = m_adapterStore.createAdapter(columnIndex);

					@Override
					public void close() throws Exception {
						m_adapter.close();
						consumer.close();
					}

					@Override
					public void accept(long index, D data) {
						m_adapter.accept(index, data);
						consumer.accept(index, data);
					}
				};
			} else {
				return consumer;
			}
		}
	}
}
