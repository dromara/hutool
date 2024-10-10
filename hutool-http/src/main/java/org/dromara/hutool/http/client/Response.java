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

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.convert.ConvertUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.http.HttpException;
import org.dromara.hutool.http.client.body.ResponseBody;
import org.dromara.hutool.http.meta.ContentTypeUtil;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.HttpHeaderUtil;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 响应内容接口，包括响应状态码、HTTP消息头、响应体等信息
 *
 * @author looly
 * @since 6.0.0
 */
public interface Response extends Closeable {

	/**
	 * 获取状态码
	 *
	 * @return 状态码
	 */
	int getStatus();

	/**
	 * 根据name获取头信息<br>
	 * 根据RFC2616规范，header的name不区分大小写
	 *
	 * @param name Header名
	 * @return Header值
	 */
	String header(final String name);

	/**
	 * 获取headers
	 *
	 * @return Headers Map
	 */
	Map<String, List<String>> headers();

	/**
	 * 获取字符集编码，默认为响应头中的编码
	 *
	 * @return 字符集
	 */
	default Charset charset() {
		return ContentTypeUtil.getCharset(header(HeaderName.CONTENT_TYPE));
	}

	/**
	 * 获得服务区响应流<br>
	 * 流获取后处理完毕需关闭此类
	 *
	 * @return 响应流
	 */
	InputStream bodyStream();

	/**
	 * 同步<br>
	 * 如果为异步状态，则暂时不读取服务器中响应的内容，而是持有Http链接的{@link InputStream}。<br>
	 * 当调用此方法时，异步状态转为同步状态，此时从Http链接流中读取body内容并暂存在内容(内存)中。如果已经是同步状态，则不进行任何操作。
	 *
	 * @return this
	 */
	Response sync();

	/**
	 * 获取响应体，包含服务端返回的内容和Content-Type信息<br>
	 * 如果为HEAD、CONNECT、TRACE等方法无响应体，则返回{@code null}
	 *
	 * @return {@link ResponseBody}
	 */
	ResponseBody body();

	/**
	 * 获取响应主体
	 *
	 * @return String
	 * @throws HttpException 包装IO异常
	 */
	default String bodyStr() throws HttpException {
		try (final ResponseBody body = body()) {
			return body.getString();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 获取响应流字节码<br>
	 * 此方法会转为同步模式，读取响应流并关闭之
	 *
	 * @return byte[]
	 */
	default byte[] bodyBytes() {
		try (final ResponseBody body = body()) {
			return null == body ? null : body.getBytes();
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}
	}

	/**
	 * 请求是否成功，判断依据为：状态码范围在200~299内。
	 *
	 * @return 是否成功请求
	 */
	default boolean isOk() {
		final int status = getStatus();
		return status >= 200 && status < 300;
	}

	/**
	 * 根据name获取头信息
	 *
	 * @param name Header名
	 * @return Header值
	 */
	default String header(final HeaderName name) {
		if (null == name) {
			return null;
		}
		return header(name.toString());
	}

	/**
	 * 根据name获取对应的头信息列表
	 *
	 * @param name Header名
	 * @return Header值
	 */
	default List<String> headerList(final String name) {
		return HttpHeaderUtil.headerList(headers(), name);
	}

	/**
	 * 获取内容编码
	 *
	 * @return String
	 */
	default String contentEncoding() {
		return header(HeaderName.CONTENT_ENCODING);
	}

	/**
	 * 获取内容长度，以下情况长度无效：
	 * <ul>
	 *     <li>Transfer-Encoding: Chunked</li>
	 *     <li>Content-Encoding: XXX</li>
	 * </ul>
	 * 参考：<a href="https://blog.csdn.net/jiang7701037/article/details/86304302">https://blog.csdn.net/jiang7701037/article/details/86304302</a>
	 *
	 * @return 长度，-1表示服务端未返回或长度无效
	 * @since 5.7.9
	 */
	default long contentLength() {
		long contentLength = ConvertUtil.toLong(header(HeaderName.CONTENT_LENGTH), -1L);
		if (contentLength > 0 && (isChunked() || StrUtil.isNotBlank(contentEncoding()))) {
			//按照HTTP协议规范，在 Transfer-Encoding和Content-Encoding设置后 Content-Length 无效。
			contentLength = -1;
		}
		return contentLength;
	}

	/**
	 * 是否为Transfer-Encoding:Chunked的内容
	 *
	 * @return 是否为Transfer-Encoding:Chunked的内容
	 * @since 4.6.2
	 */
	default boolean isChunked() {
		return "Chunked".equalsIgnoreCase(header(HeaderName.TRANSFER_ENCODING));
	}

	/**
	 * 获取本次请求服务器返回的Cookie信息
	 *
	 * @return Cookie字符串
	 * @since 3.1.1
	 */
	default String getCookieStr() {
		return header(HeaderName.SET_COOKIE);
	}

	/**
	 * 从Content-Disposition头中获取文件名，以参数名为`filename`为例，规则为：
	 * <ul>
	 *     <li>首先按照RFC5987规范检查`filename*`参数对应的值，即：`filename*="example.txt"`，则获取`example.txt`</li>
	 *     <li>如果找不到`filename*`参数，则检查`filename`参数对应的值，即：`filename="example.txt"`，则获取`example.txt`</li>
	 * </ul>
	 * 按照规范，`Content-Disposition`可能返回多个，此处遍历所有返回头，并且`filename*`始终优先获取，即使`filename`存在并更靠前。<br>
	 * 参考：https://developer.mozilla.org/zh-CN/docs/Web/HTTP/Headers/Content-Disposition
	 *
	 * @param paramName 文件参数名，如果为{@code null}则使用默认的`filename`
	 * @return 文件名，empty表示无
	 */
	default String getFileNameFromDisposition(final String paramName) {
		return HttpHeaderUtil.getFileNameFromDisposition(headers(), paramName);
	}

	/**
	 * 链式处理结果
	 *
	 * @param consumer {@link Consumer}
	 */
	default void then(final Consumer<Response> consumer) {
		consumer.accept(this);
	}
}
