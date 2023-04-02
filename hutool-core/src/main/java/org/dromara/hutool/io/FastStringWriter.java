/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.io;

import java.io.Writer;

/**
 * 借助{@link StringBuilder} 提供快读的字符串写出，相比jdk的StringWriter非线程安全，速度更快。
 *
 * @author looly
 * @since 5.3.3
 */
@SuppressWarnings("NullableProblems")
public final class FastStringWriter extends Writer {

	private static final int DEFAULT_CAPACITY = 16;

	private final StringBuilder stringBuilder;

	/**
	 * 构造
	 */
	public FastStringWriter() {
		this(DEFAULT_CAPACITY);
	}

	/**
	 * 构造
	 *
	 * @param initialSize 初始容量
	 */
	public FastStringWriter(int initialSize) {
		if (initialSize < 0) {
			initialSize = DEFAULT_CAPACITY;
		}
		this.stringBuilder = new StringBuilder(initialSize);
	}

	// region ----- append
	@Override
	public FastStringWriter append(final char c) {
		this.stringBuilder.append(c);
		return this;
	}

	@Override
	public FastStringWriter append(final CharSequence csq, final int start, final int end) {
		this.stringBuilder.append(csq, start, end);
		return this;
	}

	@Override
	public FastStringWriter append(final CharSequence csq) {
		this.stringBuilder.append(csq);
		return this;
	}
	// endregion

	// region ----- write
	@Override
	public void write(final int c) {
		this.stringBuilder.append((char) c);
	}


	@Override
	public void write(final String str) {
		this.stringBuilder.append(str);
	}


	@Override
	public void write(final String str, final int off, final int len) {
		this.stringBuilder.append(str, off, off + len);
	}


	@Override
	public void write(final char[] cbuf) {
		this.stringBuilder.append(cbuf, 0, cbuf.length);
	}


	@Override
	public void write(final char[] cbuf, final int off, final int len) {
		if ((off < 0) || (off > cbuf.length) || (len < 0) ||
				((off + len) > cbuf.length) || ((off + len) < 0)) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return;
		}
		this.stringBuilder.append(cbuf, off, len);
	}
	// endregion


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
		return this.stringBuilder.toString();
	}

}
