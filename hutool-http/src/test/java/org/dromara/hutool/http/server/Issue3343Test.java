/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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
