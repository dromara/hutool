package cn.hutool.http.server;

import cn.hutool.http.HttpUtil;

import java.io.IOException;

public class Issue3568Test {
	public static void main(String[] args) {
		HttpUtil.createServer(8888)
			.addHandler("/", httpExchange -> {
				throw new IOException("111");
			})
			.start();
	}
}
