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

package org.dromara.hutool.http.client.engine.okhttp;

import okhttp3.internal.http.HttpMethod;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.body.HttpBody;
import org.dromara.hutool.http.client.engine.EngineRequestBuilder;

/**
 * OkHttp请求构建器
 *
 * @author Looly
 * @since 6.0.0
 */
public class OkHttpRequestBuilder implements EngineRequestBuilder<okhttp3.Request> {

	/**
	 * 单例
	 */
	public static final OkHttpRequestBuilder INSTANCE = new OkHttpRequestBuilder();

	@Override
	public okhttp3.Request build(final Request message) {
		final okhttp3.Request.Builder builder = new okhttp3.Request.Builder()
			.url(message.handledUrl().toURL());

		// 填充方法
		final String method = message.method().name();
		final HttpBody body = message.handledBody();
		if (null != body || HttpMethod.requiresRequestBody(method)) {
			// okhttp中，POST等请求必须提供body，否则会抛异常，此处传空的OkHttpRequestBody
			// 为了兼容支持rest请求，在此不区分是否为GET等方法，一律按照body是否有值填充，兼容
			builder.method(method, new OkHttpRequestBody(body));
		} else {
			builder.method(method, null);
		}

		// 填充头信息
		message.headers().forEach((key, values) -> values.forEach(value -> builder.addHeader(key, value)));
		return builder.build();
	}
}
