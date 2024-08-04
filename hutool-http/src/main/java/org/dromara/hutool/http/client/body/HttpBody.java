/*
 * Copyright (c) 2024 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
	String getContentType();

	/**
	 * 获取指定编码的Content-Type，类似于：application/json;charset=UTF-8<br>
	 * 如果{@link #getContentType()} 已经包含编码信息，则编码信息一致直接返回，否则替换为指定编码。
	 *
	 * @param charset 编码
	 * @return Content-Type
	 * @see #getContentType()
	 */
	default String getContentType(final Charset charset) {
		String contentType = getContentType();
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
