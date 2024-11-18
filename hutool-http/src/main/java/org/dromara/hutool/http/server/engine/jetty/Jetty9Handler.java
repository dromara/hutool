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

package org.dromara.hutool.http.server.engine.jetty;

import org.dromara.hutool.http.server.handler.HttpHandler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jetty9版本使用的Handler
 *
 * @author Looly
 */
public class Jetty9Handler extends AbstractHandler {

	private final HttpHandler handler;

	/**
	 * 构造
	 *
	 * @param handler 处理器
	 */
	public Jetty9Handler(final HttpHandler handler) {
		this.handler = handler;
	}

	@Override
	public void handle(final String target, final Request baseRequest,
					   final HttpServletRequest request, final HttpServletResponse response) {
		handler.handle(new Jetty9Request(request), new Jetty9Response(response));
	}
}
