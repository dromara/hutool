package cn.hutool.core.io;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.func.Wrapper;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * {@link Reader} 包装
 *
 * @author looly
 */
public class ReaderWrapper extends Reader implements Wrapper<Reader> {

	protected final Reader raw;

	/**
	 * 构造
	 *
	 * @param reader {@link Reader}
	 */
	public ReaderWrapper(final Reader reader) {
		this.raw = Assert.notNull(reader);
	}

	@Override
	public Reader getRaw() {
		return this.raw;
	}

	@Override
	public int read() throws IOException {
		return raw.read();
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final CharBuffer target) throws IOException {
		return raw.read(target);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final char[] cbuf) throws IOException {
		return raw.read(cbuf);
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final char[] buffer, final int off, final int len) throws IOException {
		return raw.read(buffer, off, len);
	}

	@Override
	public void close() throws IOException {
		raw.close();
	}
}
