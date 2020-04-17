package org.knime.core.data.store;
//package org.knime.core.data.store.table;
//
//import java.util.List;
//
//import org.knime.core.data.api.column.ReadableAccess;
//import org.knime.core.data.api.column.WritableAccess;
//import org.knime.core.data.api.column.access.ReadableStructAccess;
//import org.knime.core.data.api.column.access.WritableStructAccess;
//import org.knime.core.data.store.Data;
//import org.knime.core.data.store.DataAccess;
//import org.knime.core.data.store.DataDomain;
//import org.knime.core.data.store.WritableDataStore;
//import org.knime.core.data.store.table.StructDataStore.StructDataAccess;
//
///**
// * Struct support
// */
//public class StructDataStore implements WritableDataStore<Data<?>[], StructDataAccess> {
//
//	private List<WritableDataStore<?, ?>> m_childStores;
//
//	public StructDataStore(List<WritableDataStore<?, ?>> subStores) {
//		m_childStores = subStores;
//	}
//
//	@Override
//	public void close() throws Exception {
//		for (WritableDataStore<?, ?> child : m_childStores) {
//			child.close();
//		}
//	}
//
//	@Override
//	public StructDataAccess createDataAccess() {
//		return new StructDataAccess();
//	}
//
//	@SuppressWarnings({ "rawtypes", "unchecked" })
//	@Override
//	public void add(Data<Data<?>[]> data) {
//		final Data<?>[] childData = data.get();
//		for (int i = 0; i < childData.length; i++) {
//			// TODO nicer cast
//			m_childStores.get(i).add((Data) childData[i]);
//		}
//	}
//
//	@SuppressWarnings("unchecked")
//	@Override
//	public DataCursor<Data<?>[]> cursor() {
//		return new DataCursor<Data<?>[]>() {
//
//			final DataCursor<?>[] m_children = new DataCursor[m_childStores.size()];
//
//			{
//				for (int i = 0; i < m_children.length; i++) {
//					m_children[i] = (DataCursor<?>) m_childStores.get(i).cursor();
//				}
//			}
//
//			@Override
//			public void close() throws Exception {
//				for (DataCursor<?> cursor : m_children) {
//					cursor.close();
//				}
//			}
//
//			@Override
//			public Data<Data<?>[]> get() {
//				final Data<?>[] data = new Data[m_childStores.size()];
//				for (int i = 0; i < m_children.length; i++) {
//					data[i] = (Data<Data<?>>) m_children[i].get();
//				}
//				return new StructData(data);
//			}
//
//			@Override
//			public void fwd() {
//				for (int i = 0; i < m_children.length; i++) {
//					m_children[i].fwd();
//				}
//			}
//
//			@Override
//			public boolean canFwd() {
//				return m_children[0].canFwd();
//			}
//		};
//	}
//
//	@Override
//	public StructData create() {
//		final Data<?>[] data = new Data[m_childStores.size()];
//		for (int i = 0; i < data.length; i++) {
//			data[i] = m_childStores.get(i).createData();
//		}
//		return new StructData(data);
//	}
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//	@Override
//	public void release(Data<Data<?>[]> data) {
//		final Data<?>[] childData = data.get();
//		for (int i = 0; i < childData.length; i++) {
//			// TODO nicer cast
//			m_childStores.get(i).release((Data) childData[i]);
//		}
//	}
//
//	@Override
//	public void closeForWriting() {
//		for (final WritableDataStore<?, ?> child : m_childStores) {
//			child.closeForWriting();
//		}
//	}
//
//	@Override
//	public DataDomain getDomain() {
//		throw new UnsupportedOperationException("Not yet implemented");
//	}
//
//	class StructDataAccess implements DataAccess<Data<?>[]>, ReadableStructAccess, WritableStructAccess {
//
//		final DataAccess<?>[] m_children = new DataAccess<?>[m_childStores.size()];
//
//		{
//			for (int i = 0; i < m_children.length; i++) {
//				m_children[i] = m_childStores.get(i).createDataAccess();
//			}
//		}
//
//		@Override
//		public boolean isMissing() {
//			boolean isMissing = true;
//			for (int i = 0; i < m_children.length; i++) {
//				isMissing &= m_children[i].isMissing();
//			}
//			// only missing if all are missing
//			return isMissing;
//		}
//
//		@Override
//		public void setMissing() {
//			for (int i = 0; i < m_children.length; i++) {
//				m_children[i].setMissing();
//			}
//		}
//
//		@Override
//		public WritableAccess writableValueAt(long i) {
//			return m_children[(int) i];
//		}
//
//		@Override
//		public ReadableAccess readableValueAt(long i) {
//			return m_children[(int) i];
//		}
//
//		@SuppressWarnings({ "rawtypes", "unchecked" })
//		@Override
//		public void setData(Data<?>[] data) {
//			for (int i = 0; i < m_children.length; i++) {
//				// TODO nicer cast
//				((DataAccess) m_children[i]).setData(data[i].get());
//			}
//		}
//
//		@Override
//		public void incIndex() {
//			for (int i = 0; i < m_children.length; i++) {
//				m_children[i].incIndex();
//			}
//		}
//	}
//
//	class StructData implements Data<Data<?>[]> {
//
//		private Data<?>[] m_data;
//
//		public StructData(Data<?>[] data) {
//			m_data = data;
//		}
//
//		@Override
//		public void setValueCount(long numValues) {
//			for (final Data<?> data : m_data) {
//				data.setValueCount(numValues);
//			}
//		}
//
//		@Override
//		public long getValueCount() {
//			return m_data[0].getValueCount();
//		}
//
//		@Override
//		public long getCapacity() {
//			return m_data[0].getCapacity();
//		}
//
//		@Override
//		public Data<?>[] get() {
//			return m_data;
//		}
//	}
//
//}
