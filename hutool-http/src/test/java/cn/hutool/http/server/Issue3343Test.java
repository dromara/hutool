package cn.hutool.http.server;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.map.multi.ListValueMap;
import cn.hutool.http.HttpUtil;

/**
 * http://localhost:8888/?name=hutool
 */
public class Issue3343Test {
	public static void main(final String[] args) {
		final SimpleServer server = HttpUtil.createServer(8888)
			.addFilter((req, res, chain) -> {
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
