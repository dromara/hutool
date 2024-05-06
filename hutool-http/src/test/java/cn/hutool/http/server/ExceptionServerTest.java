package cn.hutool.http.server;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.filter.DefaultExceptionFilter;

import java.io.IOException;

public class ExceptionServerTest {
	public static void main(final String[] args) {
		HttpUtil.createServer(8888)
			.addFilter(new DefaultExceptionFilter())
			.addAction("/", (req, res) -> {
				throw new RuntimeException("Test Exception");
			})
			.start();
	}
}
