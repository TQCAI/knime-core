package org.knime.core.data.impl.arrow;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;

import org.apache.arrow.memory.BufferAllocator;
import org.apache.arrow.vector.VarCharVector;
import org.knime.core.data.access.ReadableStringAccess;
import org.knime.core.data.access.WritableStringAccess;
import org.knime.core.data.impl.arrow.VarCharVectorColumnStore.VarCharVectorAccess;

public class VarCharVectorColumnStore extends AbstractColumnStore<VarCharVector, VarCharVectorAccess> {

	VarCharVectorColumnStore(BufferAllocator allocator, long chunkSize) {
		super(allocator, chunkSize);
	}

	@Override
	public VarCharVectorAccess createDataAccess() {
		return new VarCharVectorAccess();
	}

	@Override
	protected VarCharVector create(BufferAllocator allocator, long capacity) {
		final VarCharVector vector = new VarCharVector((String) null, allocator);
		// TODO: Heuristic
		vector.allocateNew(64l * capacity, (int) capacity);
		return vector;
	}

	// TODO: row key split string 'RowKey' from integer '0'
	// TODO: we could gain some performance here (i.e. avoid object creation
	// where possible)
	// TODO we don't have to encode anything in case we stay on the same system
	static final class VarCharVectorAccess //
			extends AbstractFieldVectorAccess<VarCharVector> //
			implements ReadableStringAccess, WritableStringAccess {

		private final CharsetDecoder decoder = Charset.forName("UTF-8").newDecoder()
				.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);
		private final CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder()
				.onMalformedInput(CodingErrorAction.REPLACE).onUnmappableCharacter(CodingErrorAction.REPLACE);

		@Override
		public String getStringValue() {
			try {
				return decoder.decode(ByteBuffer.wrap(m_vector.get(m_index))).toString();
			} catch (CharacterCodingException e) {
				throw new RuntimeException(e);
			}
		}

		@Override
		public void setStringValue(final String value) {
			try {
				final ByteBuffer encode = encoder.encode(CharBuffer.wrap(value.toCharArray()));
				m_vector.set(m_index, encode.array(), 0, encode.limit());
			} catch (CharacterCodingException e) {
				throw new RuntimeException(e);
			}
		}

	}
}
