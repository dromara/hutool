/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.io;

import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.lang.wrapper.Wrapper;

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
	public boolean markSupported() {
		return this.raw.markSupported();
	}

	@Override
	public void mark(final int readAheadLimit) throws IOException {
		this.raw.mark(readAheadLimit);
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.raw.skip(n);
	}

	@Override
	public boolean ready() throws IOException {
		return this.raw.ready();
	}

	@Override
	public void reset() throws IOException {
		this.raw.reset();
	}

	@Override
	public void close() throws IOException {
		raw.close();
	}
}
