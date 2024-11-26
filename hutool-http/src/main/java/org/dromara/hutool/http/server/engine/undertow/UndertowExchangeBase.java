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

package org.dromara.hutool.http.server.engine.undertow;

import io.undertow.server.HttpServerExchange;

/**
 * Undertow请求对象基类，用于获取原始Undertow请求对象
 *
 * @author looly
 * @since 6.0.0
 */
public class UndertowExchangeBase {

	final HttpServerExchange exchange;

	/**
	 * 构造
	 *
	 * @param exchange Undertow请求对象
	 */
	public UndertowExchangeBase(final HttpServerExchange exchange) {
		this.exchange = exchange;
	}

	/**
	 * 获取原始Undertow请求对象
	 *
	 * @return 原始Undertow请求对象
	 */
	public HttpServerExchange getExchange(){
		return this.exchange;
	}
}