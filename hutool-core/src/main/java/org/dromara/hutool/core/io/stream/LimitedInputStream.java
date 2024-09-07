/*
 * Copyright (c) 2013-2024 Hutool Team and hutool.cn
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

import org.dromara.hutool.core.lang.Assert;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 限制读取最大长度的{@link FilterInputStream} 实现<br>
 *
 * @author jadx
 */
public class LimitedInputStream extends FilterInputStream {

	protected long limit;
	private final boolean throwWhenReachLimit;

	/**
	 * 构造
	 *
	 * @param in                  {@link InputStream}
	 * @param limit               限制最大读取量，单位byte
	 * @param throwWhenReachLimit 是否在达到限制时抛出异常，{@code false}则读取到限制后返回-1
	 */
	public LimitedInputStream(final InputStream in, final long limit, final boolean throwWhenReachLimit) {
		super(Assert.notNull(in, "InputStream must not be null!"));
		this.limit = Math.max(0L, limit);
		this.throwWhenReachLimit = throwWhenReachLimit;
	}

	@Override
	public int read() throws IOException {
		final int data = (limit == 0) ? -1 : super.read();
		checkLimit(data);
		limit = (data < 0) ? 0 : limit - 1;
		return data;
	}

	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		final int length = (limit == 0) ? -1 : super.read(b, off, len > limit ? (int) limit : len);
		checkLimit(length);
		limit = (length < 0) ? 0 : limit - length;
		return length;
	}

	@Override
	public long skip(final long len) throws IOException {
		final long length = super.skip(Math.min(len, limit));
		checkLimit(length);
		limit -= length;
		return length;
	}

	@Override
	public int available() throws IOException {
		final int length = super.available();
		return length > limit ? (int)limit : length;
	}

	/**
	 * 检查读取数据是否达到限制
	 *
	 * @param data 读取的数据
	 * @throws IOException 定义了限制并设置达到限制后抛出异常
	 */
	private void checkLimit(final long data) throws IOException {
		if (data < 0 && limit > 0 && throwWhenReachLimit) {
			throw new IOException("Read limit exceeded");
		}
	}
}
