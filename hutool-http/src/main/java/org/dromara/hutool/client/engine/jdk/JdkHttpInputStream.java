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

package org.dromara.hutool.client.engine.jdk;

import org.dromara.hutool.GlobalCompressStreamRegister;
import org.dromara.hutool.HttpException;
import org.dromara.hutool.meta.HttpStatus;
import org.dromara.hutool.reflect.ConstructorUtil;
import org.dromara.hutool.text.StrUtil;

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
			if (false == (e instanceof FileNotFoundException)) {
				throw new HttpException(e);
			}
			// 服务器无返回内容，忽略之
		}

		// 在一些情况下，返回的流为null，此时提供状态码说明
		if (null == this.in) {
			this.in = new ByteArrayInputStream(StrUtil.format("Error request, null response with status: {}", response.status).getBytes());
			return;
		}

		final String contentEncoding = response.contentEncoding();
		final Class<? extends InputStream> streamClass = GlobalCompressStreamRegister.INSTANCE.get(contentEncoding);
		if (null != streamClass) {
			try {
				this.in = ConstructorUtil.newInstance(streamClass, this.in);
			} catch (final Exception ignore) {
				// 对于构造错误的压缩算法，跳过之
			}
		}
	}
}
