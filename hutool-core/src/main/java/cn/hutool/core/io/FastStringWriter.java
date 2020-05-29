package cn.hutool.core.io;

import cn.hutool.core.text.StrBuilder;

import java.io.Writer;

/**
 * 借助{@link StrBuilder} 提供快读的字符串写出，相比jdk的StringWriter非线程安全，速度更快。
 *
 * @author looly
 * @since 5.3.3
 */
public final class FastStringWriter extends Writer {

	private final StrBuilder builder;

	/**
	 * 构造
	 */
	public FastStringWriter() {
		this(StrBuilder.DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialSize 初始容量
	 */
	public FastStringWriter(int initialSize) {
		super();
		if (initialSize < 0) {
			initialSize = StrBuilder.DEFAULT_CAPACITY;
		}
		this.builder = new StrBuilder(initialSize);
	}


	@Override
	public void write(final int c) {
		this.builder.append((char) c);
	}


	@Override
	public void write(final String str) {
		this.builder.append(str);
	}


	@Override
	public void write(final String str, final int off, final int len) {
		this.builder.append(str, off, off + len);
	}


	@Override
	public void write(final char[] cbuf) {
		this.builder.append(cbuf, 0, cbuf.length);
	}


	@Override
	public void write(final char[] cbuf, final int off, final int len) {
		if ((off < 0) || (off > cbuf.length) || (len < 0) ||
				((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		this.builder.append(cbuf, off, len);
	}


	@Override
	public void flush() {
		// Nothing to be flushed
	}


	@Override
	public void close() {
		// Nothing to be closed
	}


	@Override
	public String toString() {
		return this.builder.toString();
	}

}