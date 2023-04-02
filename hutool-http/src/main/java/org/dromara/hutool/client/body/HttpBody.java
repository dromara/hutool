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

package org.dromara.hutool.client.body;

import org.dromara.hutool.io.stream.FastByteArrayOutputStream;
import org.dromara.hutool.io.IoUtil;

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
	 * 获取Content-Type
	 *
	 * @return Content-Type值
	 */
	String getContentType();

	/**
	 * 获取指定编码的Content-Type，类似于：application/json;charset=UTF-8
	 * @param charset 编码
	 * @return Content-Type
	 */
	default String getContentType(final Charset charset){
		final String contentType = getContentType();
		if(null == contentType){
			return null;
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
}
