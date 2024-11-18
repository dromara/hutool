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

package org.dromara.hutool.http.server.handler;

/**
 * HTTP请求处理器<br>
 * 抽象请求处理，对于不同的HTTP服务器，将这个处理器封装成对应的处理器，例如Jetty的JettyHandler，Undertow的UndertowHandler等
 *
 * @author Looly
 */
public interface HttpHandler {

	/**
	 * 处理请求
	 *
	 * @param request  请求对象
	 * @param response 响应对象
	 */
	void handle(ServerRequest request, ServerResponse response);
}
