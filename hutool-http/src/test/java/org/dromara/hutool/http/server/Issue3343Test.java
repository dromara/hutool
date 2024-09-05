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

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.multi.ListValueMap;
import org.dromara.hutool.http.HttpUtil;

/**
 * http://localhost:8888/?name=hutool
 */
public class Issue3343Test {

	public static void main(final String[] args) {
		final SimpleServer server = HttpUtil.createServer(8888)
			.addFilter((req, res, chain)->{
				Console.log(DateUtil.now() + " got request: " + req.getPath());
				Console.log(" >   from : " + req.getClientIP());
				// 过滤器中获取请求参数
				Console.log(" > params : " + req.getParams());
				chain.doFilter(req.getHttpExchange());
			});

		server.addAction("/", Issue3343Test::index);

		server.start();
	}

	private static void index(HttpServerRequest request, HttpServerResponse response) {
		// 具体逻辑中再次获取请求参数
		ListValueMap<String, String> params = request.getParams();
		Console.log("index params: " + params);
		response.getWriter().write("GOT: " + params);
	}
}
