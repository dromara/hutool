package cn.hutool.http.test;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpStatus;
import cn.hutool.http.server.SimpleServer;

import java.io.OutputStream;

public class SimpleServerTest {

	public static void main(String[] args) {
		final SimpleServer server = new SimpleServer(8888);
		server.addHandler("/", httpExchange -> {
			httpExchange.sendResponseHeaders(HttpStatus.HTTP_OK, 0);
			final OutputStream out = httpExchange.getResponseBody();
			out.write(StrUtil.bytes("Hello Hutool Server!"));
			IoUtil.close(out);
		}).start();
	}
}
