package org.knime.core.data.data.table;

public interface TableDataReadAccess {
	DataBatchLoader<?> createLoader();

	interface DataBatchLoader<D> {
		D[] load(int columnIndex);
	}
}
