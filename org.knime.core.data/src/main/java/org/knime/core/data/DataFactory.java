package org.knime.core.data;

public interface DataFactory<T> {

	DataAccess<T> createAccess();

	Data<T> createData();

	DataWriter<T> createWriter();

}
