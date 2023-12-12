/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http.server;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import org.dromara.hutool.core.array.ArrayUtil;
import org.dromara.hutool.core.io.IORuntimeException;
import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.stream.LimitedInputStream;
import org.dromara.hutool.core.map.CaseInsensitiveMap;
import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.core.net.NetUtil;
import org.dromara.hutool.core.net.multipart.MultipartFormData;
import org.dromara.hutool.core.net.multipart.UploadSetting;
import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.meta.ContentTypeUtil;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.http.useragent.UserAgent;
import org.dromara.hutool.http.useragent.UserAgentUtil;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * Http请求对象，对{@link HttpExchange}封装
 *
 * @author looly
 * @since 5.2.6
 */
public class HttpServerRequest extends HttpServerBase {

	private Map<String, HttpCookie> cookieCache;
	private ListValueMap<String, String> paramsCache;
	private MultipartFormData multipartFormDataCache;
	private Charset charsetCache;
	private byte[] bodyCache;

	/**
	 * 构造
	 *
	 * @param httpExchange {@link HttpExchange}
	 */
	public HttpServerRequest(final HttpExchange httpExchange) {
		super(httpExchange);
	}

	/**
	 * 获得Http Method
	 *
	 * @return Http Method
	 */
	public String getMethod() {
		return this.httpExchange.getRequestMethod();
	}

	/**
	 * 是否为GET请求
	 *
	 * @return 是否为GET请求
	 */
	public boolean isGetMethod() {
		return Method.GET.name().equalsIgnoreCase(getMethod());
	}

	/**
	 * 是否为POST请求
	 *
	 * @return 是否为POST请求
	 */
	public boolean isPostMethod() {
		return Method.POST.name().equalsIgnoreCase(getMethod());
	}

	/**
	 * 获得请求URI
	 *
	 * @return 请求URI
	 */
	public URI getURI() {
		return this.httpExchange.getRequestURI();
	}

	/**
	 * 获得请求路径Path
	 *
	 * @return 请求路径
	 */
	public String getPath() {
		return getURI().getPath();
	}

	/**
	 * 获取请求参数
	 *
	 * @return 参数字符串
	 */
	public String getQuery() {
		return getURI().getQuery();
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @return header值
	 */
	public Headers getHeaders() {
		return this.httpExchange.getRequestHeaders();
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerNameKey 头信息的KEY
	 * @return header值
	 */
	public String getHeader(final HeaderName headerNameKey) {
		return getHeader(headerNameKey.toString());
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerKey 头信息的KEY
	 * @return header值
	 */
	public String getHeader(final String headerKey) {
		return getHeaders().getFirst(headerKey);
	}

	/**
	 * 获得请求header中的信息
	 *
	 * @param headerKey 头信息的KEY
	 * @param charset   字符集
	 * @return header值
	 */
	public String getHeader(final String headerKey, final Charset charset) {
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
	public String getContentType() {
		return getHeader(HeaderName.CONTENT_TYPE);
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
	public Charset getCharset() {
		if(null == this.charsetCache){
			final String contentType = getContentType();
			this.charsetCache = ObjUtil.defaultIfNull(ContentTypeUtil.getCharset(contentType), DEFAULT_CHARSET);
		}

		return this.charsetCache;
	}

	/**
	 * 获得User-Agent
	 *
	 * @return User-Agent字符串
	 */
	public String getUserAgentStr() {
		return getHeader(HeaderName.USER_AGENT);
	}

	/**
	 * 获得User-Agent，未识别返回null
	 *
	 * @return User-Agent字符串，未识别返回null
	 */
	public UserAgent getUserAgent() {
		return UserAgentUtil.parse(getUserAgentStr());
	}

	/**
	 * 获得Cookie信息字符串
	 *
	 * @return cookie字符串
	 */
	public String getCookiesStr() {
		return getHeader(HeaderName.COOKIE);
	}

	/**
	 * 获得Cookie信息列表
	 *
	 * @return Cookie信息列表
	 */
	public Collection<HttpCookie> getCookies() {
		return getCookieMap().values();
	}

	/**
	 * 获得Cookie信息Map，键为Cookie名，值为HttpCookie对象
	 *
	 * @return Cookie信息Map
	 */
	public Map<String, HttpCookie> getCookieMap() {
		if (null == this.cookieCache) {
			cookieCache = MapUtil.view(MapUtil.putAll(
					new CaseInsensitiveMap<>(),
					NetUtil.parseCookies(getCookiesStr()),
					HttpCookie::getName));
		}
		return cookieCache;
	}

	/**
	 * 获得指定Cookie名对应的HttpCookie对象
	 *
	 * @param cookieName Cookie名
	 * @return HttpCookie对象
	 */
	public HttpCookie getCookie(final String cookieName) {
		return getCookieMap().get(cookieName);
	}

	/**
	 * 是否为Multipart类型表单，此类型表单用于文件上传
	 *
	 * @return 是否为Multipart类型表单，此类型表单用于文件上传
	 */
	public boolean isMultipart() {
		if (!isPostMethod()) {
			return false;
		}

		final String contentType = getContentType();
		if (StrUtil.isBlank(contentType)) {
			return false;
		}
		return contentType.toLowerCase().startsWith("multipart/");
	}

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容<br>
	 * 使用{@link #getCharset()}判断编码，判断失败使用UTF-8编码
	 *
	 * @return 请求
	 */
	public String getBody() {
		return getBody(getCharset());
	}

	/**
	 * 获取请求体文本，可以是form表单、json、xml等任意内容
	 *
	 * @param charset 编码
	 * @return 请求
	 */
	public String getBody(final Charset charset) {
		return StrUtil.str(getBodyBytes(), charset);
	}

	/**
	 * 获取body的bytes数组
	 *
	 * @return body的bytes数组
	 */
	public byte[] getBodyBytes(){
		if(null == this.bodyCache){
			this.bodyCache = IoUtil.readBytes(getBodyStream(), true);
		}
		return this.bodyCache;
	}

	/**
	 * 获取请求体的流，流中可以读取请求内容，包括请求表单数据或文件上传数据
	 *
	 * @return 流
	 */
	public InputStream getBodyStream() {
		InputStream bodyStream = this.httpExchange.getRequestBody();

		//issue#I6Q30X，读取body长度，避免读取结束后无法正常结束问题
		final String contentLengthStr = getHeader(HeaderName.CONTENT_LENGTH);
		long contentLength = 0;
		if(StrUtil.isNotBlank(contentLengthStr)){
			try{
				contentLength = Long.parseLong(contentLengthStr);
			} catch (final NumberFormatException ignore){
				// ignore
			}
		}

		if(contentLength > 0){
			bodyStream = new LimitedInputStream(bodyStream, contentLength);
		}

		return bodyStream;
	}

	/**
	 * 获取指定名称的参数值，取第一个值
	 * @param name 参数名
	 * @return 参数值
	 * @since 5.5.8
	 */
	public String getParam(final String name){
		return getParams().getValue(name, 0);
	}

	/**
	 * 获取指定名称的参数值
	 *
	 * @param name 参数名
	 * @return 参数值
	 * @since 5.5.8
	 */
	public Collection<String> getParams(final String name){
		return getParams().get(name);
	}

	/**
	 * 获取参数Map
	 *
	 * @return 参数map
	 */
	public ListValueMap<String, String> getParams() {
		if (null == this.paramsCache) {
			this.paramsCache = new ListValueMap<>();
			final Charset charset = getCharset();

			//解析URL中的参数
			final String query = getQuery();
			if(StrUtil.isNotBlank(query)){
				this.paramsCache.putAll(UrlQueryUtil.decodeQueryList(query, charset));
			}

			// 解析multipart中的参数
			if(isMultipart()){
				this.paramsCache.putAll(getMultipart().getParamListMap());
			} else{
				// 解析body中的参数
				final String body = getBody();
				if(StrUtil.isNotBlank(body)){
					this.paramsCache.putAll(UrlQueryUtil.decodeQueryList(body, charset));
				}
			}
		}

		return this.paramsCache;
	}

	/**
	 * 获取客户端IP
	 *
	 * <p>
	 * 默认检测的Header:
	 *
	 * <pre>
	 * 1、X-Forwarded-For
	 * 2、X-Real-IP
	 * 3、Proxy-Client-IP
	 * 4、WL-Proxy-Client-IP
	 * </pre>
	 *
	 * <p>
	 * otherHeaderNames参数用于自定义检测的Header<br>
	 * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
	 * </p>
	 *
	 * @param otherHeaderNames 其他自定义头文件，通常在Http服务器（例如Nginx）中配置
	 * @return IP地址
	 */
	public String getClientIP(final String... otherHeaderNames) {
		String[] headers = {"X-Forwarded-For", "X-Real-IP", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"};
		if (ArrayUtil.isNotEmpty(otherHeaderNames)) {
			headers = ArrayUtil.addAll(headers, otherHeaderNames);
		}

		return getClientIPByHeader(headers);
	}

	/**
	 * 获取客户端IP
	 *
	 * <p>
	 * headerNames参数用于自定义检测的Header<br>
	 * 需要注意的是，使用此方法获取的客户IP地址必须在Http服务器（例如Nginx）中配置头信息，否则容易造成IP伪造。
	 * </p>
	 *
	 * @param headerNames 自定义头，通常在Http服务器（例如Nginx）中配置
	 * @return IP地址
	 * @since 4.4.1
	 */
	public String getClientIPByHeader(final String... headerNames) {
		String ip;
		for (final String header : headerNames) {
			ip = getHeader(header);
			if (!NetUtil.isUnknown(ip)) {
				return NetUtil.getMultistageReverseProxyIp(ip);
			}
		}

		ip = this.httpExchange.getRemoteAddress().getHostName();
		return NetUtil.getMultistageReverseProxyIp(ip);
	}

	/**
	 * 获得MultiPart表单内容，多用于获得上传的文件
	 *
	 * @return MultipartFormData
	 * @throws IORuntimeException IO异常
	 * @since 5.3.0
	 */
	public MultipartFormData getMultipart() throws IORuntimeException {
		if(null == this.multipartFormDataCache){
			this.multipartFormDataCache = parseMultipart(new UploadSetting());
		}
		return this.multipartFormDataCache;
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
	public MultipartFormData parseMultipart(final UploadSetting uploadSetting) throws IORuntimeException {
		final MultipartFormData formData = new MultipartFormData(uploadSetting);
		try {
			formData.parseRequestStream(getBodyStream(), getCharset());
		} catch (final IOException e) {
			throw new IORuntimeException(e);
		}

		return formData;
	}
}
