package org.knime.core.data.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.knime.core.data.api.NativeType;
import org.knime.core.data.api.column.domain.Domain;
import org.knime.core.data.api.column.domain.MutableDomain;

// TODO calculate asynchronously.
public class DataDomainAdapter implements ConsumingDataStoreAdapter {

	private final List<DataConsumer<?>> m_adapters;
	private final Map<Long, Domain> m_domains;

	public DataDomainAdapter(NativeType<?, ?>[] types) {
		m_domains = new HashMap<Long, Domain>();
		m_adapters = new ArrayList<DataConsumer<?>>(types.length);
		for (long i = 0; i < types.length; i++) {
			if (types[(int) i].hasDomain()) {
				final MutableDomain<?> domain = types[(int) i].createEmptyDomain();
				m_adapters.add(create(domain));
				m_domains.put(i, domain);
			}
		}
	}

	private <D extends Data> DataConsumer<D> create(final MutableDomain<D> domain) {
		return new DataConsumer<D>() {

			@Override
			public void accept(D data) {
				// we don't care about the index here.
				domain.update(data);
			}
		};
	}

	public Map<Long, Domain> getDomains() {
		return m_domains;
	}

	@Override
	public boolean hasAdapter(long index) {
		return false;
	}

	@Override
	public <D extends Data> DataConsumer<D> createAdapter(long index) {
		@SuppressWarnings("unchecked")
		final DataConsumer<D> dataConsumer = (DataConsumer<D>) m_adapters.get((int) index);
		return dataConsumer;
	}
}
