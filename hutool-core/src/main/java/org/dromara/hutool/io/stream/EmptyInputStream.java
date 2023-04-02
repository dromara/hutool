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

package org.dromara.hutool.io.stream;

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
