/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
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

package org.dromara.hutool.http.server;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.net.url.UrlEncoder;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ByteUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.meta.HeaderName;

import java.io.*;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 服务端响应接口，用于写出数据
 *
 * @author Looly
 */
public interface ServerResponse {

	/**
	 * 默认编码，用于获取请求头和响应头编码，默认为UTF-8
	 */
	Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	/**
	 * 设置状态码
	 *
	 * @param statusCode 状态码
	 * @return this
	 */
	ServerResponse setStatus(int statusCode);

	/**
	 * 设置编码，默认为UTF-8
	 *
	 * @param charset 编码
	 * @return this
	 */
	ServerResponse setCharset(Charset charset);

	/**
	 * 获取编码，默认为UTF-8
	 *
	 * @return 编码
	 */
	Charset getCharset();

	/**
	 * 添加响应头，如果已经存在，则追加
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	ServerResponse addHeader(final String header, final String value);

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值
	 * @return this
	 */
	ServerResponse setHeader(final String header, final String value);

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param headerName 头key
	 * @param value      值
	 * @return this
	 */
	default ServerResponse setHeader(final HeaderName headerName, final String value) {
		return setHeader(headerName.getValue(), value);
	}

	/**
	 * 设置响应头，如果已经存在，则覆盖
	 *
	 * @param header 头key
	 * @param value  值列表，如果为空，删除该header
	 * @return this
	 */
	default ServerResponse setHeader(final String header, final List<String> value) {
		// 去除原有header
		setHeader(header, (String) null);
		if(null == value){
			return this;
		}

		// 加入新的header
		for (final String valueItem : value) {
			addHeader(header, valueItem);
		}
		return this;
	}

	/**
	 * 设置Content-Type头，类似于:text/html;charset=utf-8<br>
	 * 如果用户传入的信息无charset信息，自动根据charset补充，charset设置见{@link #setCharset(Charset)}
	 *
	 * @param contentType Content-Type头内容
	 * @return this
	 */
	default ServerResponse setContentType(String contentType) {
		if (null != contentType) {
			final Charset charset = getCharset();
			if (null != charset && !contentType.contains(";charset=")) {
				contentType = ContentType.build(contentType, charset);
			}
		}

		return setHeader(HeaderName.CONTENT_TYPE, contentType);
	}

	/**
	 * 设置Content-Length头，-1表示移除头
	 *
	 * @param contentLength Content-Length头内容
	 * @return this
	 */
	default ServerResponse setContentLength(final long contentLength) {
		return setHeader(HeaderName.CONTENT_LENGTH,
			contentLength < 0 ? null : String.valueOf(contentLength));
	}

	/**
	 * 获取输出流，用于写出数据
	 *
	 * @return 输出流
	 */
	OutputStream getOutputStream();

	/**
	 * 获取响应数据流
	 *
	 * @return 响应数据流
	 */
	default PrintWriter getWriter() {
		final Charset charset = ObjUtil.defaultIfNull(getCharset(), DEFAULT_CHARSET);
		return new PrintWriter(new OutputStreamWriter(getOutputStream(), charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType Content-Type类型
	 * @return this
	 */
	default ServerResponse write(final String data, final String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	default ServerResponse write(final String data) {
		final Charset charset = ObjUtil.defaultIfNull(getCharset(), DEFAULT_CHARSET);
		return write(ByteUtil.toBytes(data, charset));
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data        数据
	 * @param contentType 返回的类型
	 * @return this
	 */
	default ServerResponse write(final byte[] data, final String contentType) {
		setContentType(contentType);
		return write(data);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param data 数据
	 * @return this
	 */
	default ServerResponse write(final byte[] data) {
		final ByteArrayInputStream in = new ByteArrayInputStream(data);
		return write(in, in.available());
	}

	/**
	 * 返回数据给客户端
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @return this
	 * @since 5.2.6
	 */
	default ServerResponse write(final InputStream in, final String contentType) {
		return write(in, 0, contentType);
	}

	/**
	 * 返回数据给客户端
	 *
	 * @param in          需要返回客户端的内容
	 * @param length      内容长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @param contentType 返回的类型
	 * @return this
	 * @since 5.2.7
	 */
	default ServerResponse write(final InputStream in, final int length, final String contentType) {
		setContentType(contentType);
		return write(in, length);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in 数据流
	 * @return this
	 */
	default ServerResponse write(final InputStream in) {
		return write(in, 0);
	}

	/**
	 * 写出数据到客户端
	 *
	 * @param in     数据流
	 * @param length 指定响应内容长度，默认0表示不定长度，会输出Transfer-encoding: chunked
	 * @return this
	 */
	default ServerResponse write(final InputStream in, final int length) {
		setContentLength(length);
		OutputStream out = null;
		try {
			out = getOutputStream();
			IoUtil.copy(in, out);
		} finally {
			IoUtil.closeQuietly(out);
			IoUtil.closeQuietly(in);
		}
		return this;
	}

	/**
	 * 返回文件给客户端（文件下载）
	 *
	 * @param file 写出的文件对象
	 * @return this
	 * @since 5.2.6
	 */
	default ServerResponse write(final File file) {
		return write(file, null);
	}

	/**
	 * 返回文件给客户端（文件下载）
	 *
	 * @param file     写出的文件对象
	 * @param fileName 文件名
	 * @return this
	 */
	default ServerResponse write(final File file, String fileName) {
		final long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			throw new IllegalArgumentException("File size is too bigger than " + Integer.MAX_VALUE);
		}

		if (StrUtil.isBlank(fileName)) {
			fileName = file.getName();
		}
		final String contentType = FileUtil.getMimeType(fileName, ContentType.OCTET_STREAM.getValue());
		BufferedInputStream in = null;
		try {
			in = FileUtil.getInputStream(file);
			write(in, (int) fileSize, contentType, fileName);
		} finally {
			IoUtil.closeQuietly(in);
		}
		return this;
	}

	/**
	 * 返回文件数据给客户端（文件下载）
	 *
	 * @param in          需要返回客户端的内容
	 * @param contentType 返回的类型
	 * @param fileName    文件名
	 * @return this
	 */
	default ServerResponse write(final InputStream in, final String contentType, final String fileName) {
		return write(in, 0, contentType, fileName);
	}

	/**
	 * 返回文件数据给客户端（文件下载）
	 *
	 * @param in          需要返回客户端的内容
	 * @param length      长度
	 * @param contentType 返回的类型
	 * @param fileName    文件名
	 * @return this
	 */
	default ServerResponse write(final InputStream in, final int length, final String contentType, final String fileName) {
		final Charset charset = ObjUtil.defaultIfNull(getCharset(), DEFAULT_CHARSET);

		if (!contentType.startsWith("text/")) {
			// 非文本类型数据直接走下载
			setHeader(HeaderName.CONTENT_DISPOSITION, StrUtil.format("attachment;filename={}", UrlEncoder.encodeAll(fileName, charset)));
		}
		return write(in, length, contentType);
	}
}
