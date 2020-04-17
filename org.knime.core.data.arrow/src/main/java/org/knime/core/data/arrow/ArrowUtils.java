
package org.knime.core.data.arrow;

import java.util.ArrayList;
import java.util.List;

import org.apache.arrow.vector.ValueVector;
import org.apache.arrow.vector.types.FloatingPointPrecision;
import org.apache.arrow.vector.types.pojo.ArrowType;
import org.apache.arrow.vector.types.pojo.Field;
import org.knime.core.data.api.column.ColumnType;
import org.knime.core.data.api.column.PrimitiveType;

import io.netty.buffer.ArrowBuf;

public final class ArrowUtils {

	private ArrowUtils() {
	}

	public static void retainVector(final ValueVector vector) {
		for (final ArrowBuf buffer : vector.getBuffers(false)) {
			buffer.getReferenceManager().retain();
		}
	}

	public static void releaseVector(final ValueVector vector) {
		for (final ArrowBuf buffer : vector.getBuffers(false)) {
			buffer.getReferenceManager().release();
		}
	}

	public static Field toField(String name, PrimitiveType type) {
		switch (type) {
		case BOOLEAN:
			return Field.nullablePrimitive(name, new ArrowType.Binary());
		case DOUBLE:
			return Field.nullablePrimitive(name, new ArrowType.FloatingPoint(FloatingPointPrecision.DOUBLE));
		case STRING:
			return Field.nullablePrimitive(name, new ArrowType.Utf8());
		default:
			throw new IllegalArgumentException("NativeType not yet implemented: " + type);
		}
	}

	public static List<Field> toFieldList(String name, ColumnType colType) {
		final List<Field> fields = new ArrayList<>();
		final PrimitiveType[] types = colType.getPrimitiveTypes();
		for (int i = 0; i < types.length; i++) {
			// TODO no idea about good naming
			fields.add(toField(name + ", index " + i + ", type, " + types[i], types[i]));
		}
		return fields;
	}

//
//	public void 
//    for (FieldVector vector : root.getFieldVectors()) {
//        appendNodes(vector, nodes, buffers);
//      }
//	
//	// TODO: Copied from org.apache.arrow.vector.VectorUnloader. Is any better
//	// way?
//	public void appendNodes(final FieldVector vector, final List<ArrowFieldNode> nodes, final List<ArrowBuf> buffers) {
//		nodes.add(new ArrowFieldNode(vector.getValueCount(), vector.getNullCount()));
//		final List<ArrowBuf> fieldBuffers = vector.getFieldBuffers();
//		final int expectedBufferCount = TypeLayout.getTypeBufferCount(vector.getField().getType());
//		if (fieldBuffers.size() != expectedBufferCount) {
//			throw new IllegalArgumentException(
//					String.format("wrong number of buffers for field %s in vector %s. found: %s", vector.getField(),
//							vector.getClass().getSimpleName(), fieldBuffers));
//		}
//		buffers.addAll(fieldBuffers);
//		for (final FieldVector child : vector.getChildrenFromFields()) {
//			appendNodes(child, nodes, buffers);
//		}
//	}
}
