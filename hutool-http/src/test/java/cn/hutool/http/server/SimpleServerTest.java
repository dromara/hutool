package cn.hutool.http.server;

import cn.hutool.http.HttpUtil;
import cn.hutool.http.server.handler.RootHandler;

public class SimpleServerTest {

	public static void main(String[] args) {
		HttpUtil.createServer(8888)
				.addHandler("/", new RootHandler("D:\\test"))
				.start();
	}
}
