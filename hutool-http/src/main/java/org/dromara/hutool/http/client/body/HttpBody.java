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

package org.dromara.hutool.http.client.body;

import org.dromara.hutool.core.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.text.StrUtil;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;

/**
 * 定义请求体接口
 */
public interface HttpBody {

	/**
	 * 写出数据，不关闭流
	 *
	 * @param out out流
	 */
	void write(OutputStream out);

	/**
	 * 获取Content-Type<br>
	 * 根据实现不同，Content-Type可能包含编码信息，也可以不包含
	 *
	 * @return Content-Type值
	 */
	String contentType();

	/**
	 * 获取写出字节长度，未知为-1
	 *
	 * @return 长度
	 */
	default long contentLength() {
		return -1;
	}

	/**
	 * 获取指定编码的Content-Type，类似于：application/json;charset=UTF-8<br>
	 * 如果{@link #contentType()} 已经包含编码信息，则编码信息一致直接返回，否则替换为指定编码。
	 *
	 * @param charset 编码
	 * @return Content-Type
	 * @see #contentType()
	 */
	default String contentType(final Charset charset) {
		String contentType = contentType();
		if (null == contentType) {
			return null;
		}

		final String charsetName = charset.name();
		if (StrUtil.endWithIgnoreCase(contentType, charsetName) || StrUtil.containsIgnoreCase(contentType, "boundary=")) {
			// 已经包含编码信息，且编码一致，无需再次添加
			// multipart无需添加charset
			return contentType;
		}

		if (StrUtil.containsIgnoreCase(contentType, ";charset=")) {
			// 已经包含编码信息，但编码不一致，需要替换
			contentType = StrUtil.subBefore(contentType, ";charset=", true);
		}

		return contentType + ";charset=" + charset.name();
	}

	/**
	 * 写出并关闭{@link OutputStream}
	 *
	 * @param out {@link OutputStream}
	 * @since 5.7.17
	 */
	default void writeClose(final OutputStream out) {
		try {
			write(out);
		} finally {
			IoUtil.closeQuietly(out);
		}
	}

	/**
	 * 获取body资源流
	 *
	 * @return {@link InputStream}
	 */
	default InputStream getStream() {
		final FastByteArrayOutputStream out = new FastByteArrayOutputStream();
		writeClose(out);
		return IoUtil.toStream(out);
	}

	/**
	 * 获取请求（响应）体字节码
	 *
	 * @return 请求体字节码
	 */
	default byte[] getBytes() {
		final InputStream bodyStream = getStream();
		if (bodyStream == null) {
			return null;
		}
		return IoUtil.readBytes(bodyStream);
	}
}
