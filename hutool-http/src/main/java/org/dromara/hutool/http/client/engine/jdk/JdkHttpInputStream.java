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

package org.dromara.hutool.http.client.engine.jdk;

import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.GlobalCompressStreamRegister;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.meta.HttpStatus;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * HTTP输入流，此流用于包装Http请求响应内容的流，用于解析各种压缩、分段的响应流内容
 *
 * @author Looly
 */
public class JdkHttpInputStream extends InputStream {

	/**
	 * 原始流
	 */
	private InputStream in;

	/**
	 * 构造
	 *
	 * @param response 响应对象
	 */
	public JdkHttpInputStream(final JdkHttpResponse response) {
		init(response);
	}

	@Override
	public int read() throws IOException {
		return this.in.read();
	}

	@SuppressWarnings("NullableProblems")
	@Override
	public int read(final byte[] b, final int off, final int len) throws IOException {
		return this.in.read(b, off, len);
	}

	@Override
	public long skip(final long n) throws IOException {
		return this.in.skip(n);
	}

	@Override
	public int available() throws IOException {
		return this.in.available();
	}

	@Override
	public void close() throws IOException {
		this.in.close();
	}

	@Override
	public synchronized void mark(final int readlimit) {
		this.in.mark(readlimit);
	}

	@Override
	public synchronized void reset() throws IOException {
		this.in.reset();
	}

	@Override
	public boolean markSupported() {
		return this.in.markSupported();
	}

	/**
	 * 初始化流
	 *
	 * @param response 响应对象
	 */
	private void init(final JdkHttpResponse response) {
		try {
			this.in = (response.status < HttpStatus.HTTP_BAD_REQUEST) ? response.httpConnection.getInputStream() : response.httpConnection.getErrorStream();
		} catch (final IOException e) {
			if (!(e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}

		// 在一些情况下，返回的流为null，此时提供状态码说明
		if (null == this.in) {
			this.in = new ByteArrayInputStream(StrUtil.format("Error request, null response with status: {}", response.status).getBytes());
			return;
		}

		this.in = GlobalCompressStreamRegister.INSTANCE.wrapStream(this.in, response.contentEncoding());
	}
}
