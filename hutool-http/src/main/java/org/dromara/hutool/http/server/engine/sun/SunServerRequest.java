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

package org.dromara.hutool.http.server.engine.sun;

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
import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.http.meta.ContentTypeUtil;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.multipart.MultipartFormData;
import org.dromara.hutool.http.multipart.UploadSetting;
import org.dromara.hutool.http.server.ServerRequest;

import java.io.InputStream;
import java.net.HttpCookie;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.Map;

/**
 * Sun Http请求包装
 *
 * @author Looly
 */
public class SunServerRequest extends SunServerBase implements ServerRequest {

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
	public SunServerRequest(final HttpExchange httpExchange) {
		super(httpExchange);
	}

	@Override
	public String getMethod() {
		return this.httpExchange.getRequestMethod();
	}

	/**
	 * 获得请求URI
	 *
	 * @return 请求URI
	 */
	public URI getURI() {
		return this.httpExchange.getRequestURI();
	}

	@Override
	public String getPath() {
		return getURI().getPath();
	}

	@Override
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

	@Override
	public String getHeader(final String name) {
		return getHeaders().getFirst(name);
	}

	@Override
	public Charset getCharset() {
		if(null == this.charsetCache){
			final String contentType = getContentType();
			this.charsetCache = ObjUtil.defaultIfNull(ContentTypeUtil.getCharset(contentType), DEFAULT_CHARSET);
		}

		return this.charsetCache;
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

	@Override
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

	@Override
	public byte[] getBodyBytes(){
		if(null == this.bodyCache){
			this.bodyCache = IoUtil.readBytes(getBodyStream(), true);
		}
		return this.bodyCache;
	}

	@Override
	public MultipartFormData getMultipart() throws IORuntimeException {
		if(null == this.multipartFormDataCache){
			this.multipartFormDataCache = parseMultipart(new UploadSetting());
		}
		return this.multipartFormDataCache;
	}

	@Override
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
}
