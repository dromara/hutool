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

import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.meta.ContentTypeUtil;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.http.multipart.MultipartFormData;
import org.dromara.hutool.http.multipart.UploadSetting;
import org.dromara.hutool.http.useragent.UserAgent;
import org.dromara.hutool.http.useragent.UserAgentUtil;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Collection;

/**
 * 服务端请求对象，用于获取请求参数等
 *
 * @author looly
 * @since 6.0.0
 */
public interface ServerRequest {

	/**
	 * 默认编码，用于获取请求头和响应头编码，默认为UTF-8
	 */
	Charset DEFAULT_CHARSET = CharsetUtil.UTF_8;

	// region ----- method

	/**
	 * 获取请求方法
	 *
	 * @return 请求方法
	 */
	String getMethod();

	/**
	 * 是否为GET请求
	 *
	 * @return 是否为GET请求
	 */
	default boolean isGetMethod() {
		return Method.GET.name().equalsIgnoreCase(getMethod());
	}

	/**
	 * 是否为POST请求
	 *
	 * @return 是否为POST请求
	 */
	default boolean isPostMethod() {
		return Method.POST.name().equalsIgnoreCase(getMethod());
	}
	// endregion

	/**
	 * 获取请求路径，包含请求参数部分
	 *
	 * @return 请求路径，包含请求参数部分
	 */
	String getPath();

	/**
	 * 获取请求参数，包括pathVariable和queryString
	 *
	 * @return 请求参数，包括pathVariable和queryString
	 */
	String getQuery();

	// region ----- header

	/**
	 * 获取请求头
	 *
	 * @param name 请求头名
	 * @return 请求头
	 */
	String getHeader(final String name);

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerNameKey 头信息的KEY
	 * @return header值
	 */
	default String getHeader(final HeaderName headerNameKey) {
		return getHeader(headerNameKey.toString());
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerKey 头信息的KEY
	 * @param charset   字符集
	 * @return header值
	 */
	default String getHeader(final String headerKey, final Charset charset) {
		final String header = getHeader(headerKey);
		if (null != header) {
			return CharsetUtil.convert(header, CharsetUtil.ISO_8859_1, charset);
		}
		return null;
	}

	/**
	 * 获取Content-Type头信息
	 *
	 * @return Content-Type头信息
	 */
	default String getContentType() {
		return getHeader(HeaderName.CONTENT_TYPE);
	}

	/**
	 * 获取Content-Length头信息，单位：字节
	 *
	 * @return Content-Length头信息，单位：字节
	 */
	default long getContentLength() {
		final String contentLength = getHeader(HeaderName.CONTENT_LENGTH);
		return StrUtil.isEmpty(contentLength) ? -1 : Long.parseLong(contentLength);
	}

	/**
	 * 获取编码，获取失败默认使用UTF-8，获取规则如下：
	 *
	 * <pre>
	 *     1、从Content-Type头中获取编码，类似于：text/html;charset=utf-8
	 * </pre>
	 *
	 * @return 编码，默认UTF-8
	 */
	default Charset getCharset() {
		final String contentType = getContentType();
		return ObjUtil.defaultIfNull(ContentTypeUtil.getCharset(contentType), DEFAULT_CHARSET);
	}

	/**
	 * 获得User-Agent
	 *
	 * @return User-Agent字符串
	 */
	default String getUserAgentStr() {
		return getHeader(HeaderName.USER_AGENT);
	}

	/**
	 * 获得User-Agent，未识别返回null
	 *
	 * @return User-Agent字符串，未识别返回null
	 */
	default UserAgent getUserAgent() {
		return UserAgentUtil.parse(getUserAgentStr());
	}

	/**
	 * 获得Cookie信息字符串
	 *
	 * @return cookie字符串
	 */
	default String getCookiesStr() {
		return getHeader(HeaderName.COOKIE);
	}

	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 *
	 * @return 是否为Multipart类型表单，此类型表单用于文件上传
	 */
	default boolean isMultipart() {
		if (!isPostMethod()) {
			return false;
		}

		final String contentType = getContentType();
		if (StrUtil.isBlank(contentType)) {
			return false;
		}
		return contentType.toLowerCase().startsWith("multipart/");
	}
	// endregion

	// region ----- body

	/**
	 * 获取请求体流
	 *
	 * @return 请求体流
	 */
	InputStream getBodyStream();

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容<br>
	 * 使用{@link #getCharset()}判断编码，判断失败使用UTF-8编码
	 *
	 * @return 请求
	 */
	default String getBody() {
		return getBody(getCharset());
	}

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容
	 *
	 * @param charset 编码
	 * @return 请求
	 */
	default String getBody(final Charset charset) {
		return StrUtil.str(getBodyBytes(), charset);
	}

	/**
	 * 获取body的bytes数组
	 *
	 * @return body的bytes数组
	 */
	default byte[] getBodyBytes() {
		return IoUtil.readBytes(getBodyStream(), true);
	}

	/**
	 * 获得MultiPart表单内容，多用于获得上传的文件
	 *
	 * @return MultipartFormData
	 * @throws IORuntimeException IO异常
	 * @since 5.3.0
	 */
	default MultipartFormData getMultipart() throws IORuntimeException {
		return parseMultipart(new UploadSetting());
	}

	/**
	 * 获得multipart/form-data 表单内容<br>
	 * 包括文件和普通表单数据<br>
	 * 在同一次请求中，此方法只能被执行一次！
	 *
	 * @param uploadSetting 上传文件的设定，包括最大文件大小、保存在内存的边界大小、临时目录、扩展名限定等
	 * @return MultiPart表单
	 * @throws IORuntimeException IO异常
	 * @since 5.3.0
	 */
	default MultipartFormData parseMultipart(final UploadSetting uploadSetting) throws IORuntimeException {
		final MultipartFormData formData = new MultipartFormData(uploadSetting);
		try {
			formData.parseRequestStream(getBodyStream(), getCharset());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return formData;
	}
	// endregion

	// region ----- param
	/**
	 * 获取指定名称的参数值，取第一个值
	 * @param name 参数名
	 * @return 参数值
	 * @since 5.5.8
	 */
	default String getParam(final String name){
		return getParams().getValue(name, 0);
	}

	/**
	 * 获取指定名称的参数值
	 *
	 * @param name 参数名
	 * @return 参数值
	 * @since 5.5.8
	 */
	default Collection<String> getParams(final String name){
		return getParams().get(name);
	}

	/**
	 * 获取参数Map
	 *
	 * @return 参数map
	 */
	default ListValueMap<String, String> getParams() {
		final ListValueMap<String, String> params = new ListValueMap<>();
		final Charset charset = getCharset();

		//解析URL中的参数
		final String query = getQuery();
		if(StrUtil.isNotBlank(query)){
			params.putAll(UrlQueryUtil.decodeQueryList(query, charset));
		}

		// 解析multipart中的参数
		if(isMultipart()){
			params.putAll(getMultipart().getParamListMap());
		} else{
			// 解析body中的参数
			final String body = getBody();
			if(StrUtil.isNotBlank(body)){
				params.putAll(UrlQueryUtil.decodeQueryList(body, charset));
			}
		}
		return params;
	}
	// endregion
}
