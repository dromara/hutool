/*
 * Copyright (c) 2013-2024 Hutool Team.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

	/**
	 * 原始Reader
	 */
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
