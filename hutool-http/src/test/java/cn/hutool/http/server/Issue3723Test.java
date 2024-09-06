package cn.hutool.http.server;

import cn.hutool.core.util.IdUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;

public class Issue3723Test {
	public static void main(String[] args) {
		SimpleServer server = HttpUtil.createServer(8888);
		server.addFilter((req, res, chain) -> {
			String requestId = IdUtil.fastSimpleUUID();
			req.getHttpExchange().setAttribute("requestId", requestId);
			res.addHeader("X-Request-Id", requestId);

			res.write("new Content");

			chain.doFilter(req.getHttpExchange());
		});
		server.addAction("/", (req, res)-> res.write("Hello Hutool Server", ContentType.JSON.getValue()));
		server.start();
	}
}
