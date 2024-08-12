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

package org.dromara.hutool.core.io.stream;

import java.io.InputStream;

/**
 * 空的流
 *
 * @author looly
 */
@SuppressWarnings("NullableProblems")
public final class EmptyInputStream extends InputStream {
	/**
	 * 单例实例
	 */
	public static final EmptyInputStream INSTANCE = new EmptyInputStream();

	private EmptyInputStream() {
	}

	@Override
	public int available() {
		return 0;
	}

	@Override
	public void close() {
	}

	@Override
	public void mark(final int readLimit) {
	}

	@Override
	public boolean markSupported() {
		return true;
	}

	@Override
	public int read() {
		return -1;
	}

	@Override
	public int read(final byte[] buf) {
		return -1;
	}

	@Override
	public int read(final byte[] buf, final int off, final int len) {
		return -1;
	}

	@Override
	public void reset() {
	}

	@Override
	public long skip(final long n) {
		return 0L;
	}
}
