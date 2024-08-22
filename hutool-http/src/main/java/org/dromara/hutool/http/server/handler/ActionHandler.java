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

package org.dromara.hutool.http.server.handler;

import org.dromara.hutool.http.server.HttpExchangeWrapper;
import org.dromara.hutool.http.server.HttpServerRequest;
import org.dromara.hutool.http.server.HttpServerResponse;
import org.dromara.hutool.http.server.action.Action;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;

/**
 * Action处理器，用于将HttpHandler转换为Action形式
 *
 * @author looly
 * @since 5.2.6
 */
public class ActionHandler implements HttpHandler {

	private final Action action;

	/**
	 * 构造
	 *
	 * @param action Action
	 */
	public ActionHandler(final Action action) {
		this.action = action;
	}

	@Override
	public void handle(final HttpExchange httpExchange) throws IOException {
		final HttpServerRequest request;
		final HttpServerResponse response;
		if (httpExchange instanceof HttpExchangeWrapper) {
			// issue#3343 当使用Filter时，可能读取了请求参数，此时使用共享的req和res，可复用缓存
			final HttpExchangeWrapper wrapper = (HttpExchangeWrapper) httpExchange;
			request = wrapper.getRequest();
			response = wrapper.getResponse();
		} else {
			request = new HttpServerRequest(httpExchange);
			response = new HttpServerResponse(httpExchange);
		}
		action.doAction(request, response);
		httpExchange.close();
	}
}
