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

package org.dromara.hutool.http.client.engine.httpclient4;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.dromara.hutool.core.lang.Assert;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.EngineRequestBuilder;
import org.dromara.hutool.http.meta.HeaderName;

/**
 * HttpClient4请求构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public class HttpUriRequestBuilder implements EngineRequestBuilder<HttpUriRequest> {

	/**
	 * 单例
	 */
	public static final HttpUriRequestBuilder INSTANCE = new HttpUriRequestBuilder();

	@Override
	public HttpUriRequest build(final Request message) {
		final UrlBuilder url = message.handledUrl();
		Assert.notNull(url, "Request URL must be not null!");

		final RequestBuilder requestBuilder = RequestBuilder
			.create(message.method().name())
			.setUri(url.toURI());

		// 自定义单次请求配置
		requestBuilder.setConfig(buildRequestConfig(message));

		// 填充自定义头
		message.headers().forEach((k, v1) -> v1.forEach((v2) -> requestBuilder.addHeader(k, v2)));

		// 填充自定义消息体
		final HttpBody body = message.handledBody();
		if (null != body) {
			requestBuilder.setEntity(new HttpClient4BodyEntity(
				// 用户自定义的内容类型
				message.header(HeaderName.CONTENT_TYPE),
				message.contentEncoding(),
				message.isChunked(),
				body));
		}

		return requestBuilder.build();
	}

	/**
	 * 构建请求配置，包括重定向
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
