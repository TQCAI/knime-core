package org.knime.core.data.record;

public interface RecordReaderConfig {
	/**
	 * TODO implement as ranges, e.g. return a ColumnIndicesSelection with a method
	 * called 'contains(int i)'?
	 * 
	 * @return the selected column indices in ascending order
	 */
	int[] getColumnIndices();
}
