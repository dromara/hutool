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

package org.dromara.hutool.http.client.engine.httpclient5;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.core5.http.ClassicHttpRequest;
import org.apache.hc.core5.http.Header;
import org.apache.hc.core5.http.message.BasicHeader;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.EngineRequestBuilder;
import org.dromara.hutool.http.meta.HeaderName;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * HttpClient5请求构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public class ClassicHttpRequestBuilder implements EngineRequestBuilder<ClassicHttpRequest> {

	/**
	 * 单例
	 */
	public static ClassicHttpRequestBuilder INSTANCE = new ClassicHttpRequestBuilder();

	@Override
	public ClassicHttpRequest build(final Request message) {
		final UrlBuilder url = message.handledUrl();
		Assert.notNull(url, "Request URL must be not null!");

		final HttpUriRequestBase request = new HttpUriRequestBase(message.method().name(), url.toURI());

		// 自定义单次请求配置
		request.setConfig(buildRequestConfig(message));

		// 填充自定义头
		request.setHeaders(toHeaderList(message.headers()).toArray(new Header[0]));

		// 填充自定义消息体
		final HttpBody body = message.handledBody();
		if (null != body) {
			request.setEntity(new HttpClient5BodyEntity(
				// 用户自定义的内容类型
				message.header(HeaderName.CONTENT_TYPE),
				message.contentEncoding(),
				message.isChunked(),
				body));
		}

		return request;
	}

	/**
	 * 获取默认头列表
	 *
	 * @return 默认头列表
	 */
	private static List<Header> toHeaderList(final Map<String, ? extends Collection<String>> headersMap) {
		final List<Header> result = new ArrayList<>();
		headersMap.forEach((k, v1) -> v1.forEach((v2) -> result.add(new BasicHeader(k, v2))));
		return result;
	}

	/**
	 * 构建请求配置，包括重定向配置
	 *
	 * @param request 请求
	 * @return {@link RequestConfig}
	 */
	private static RequestConfig buildRequestConfig(final Request request) {
		final RequestConfig.Builder requestConfigBuilder = RequestConfig.custom();
		final int maxRedirects = request.maxRedirects();
		if (maxRedirects > 0) {
			requestConfigBuilder.setMaxRedirects(maxRedirects);
		} else {
			requestConfigBuilder.setRedirectsEnabled(false);
		}

		return requestConfigBuilder.build();
	}
}
