package cn.hutool.http.server;

import cn.hutool.http.HttpUtil;

public class BlankServerTest {
	public static void main(String[] args) {
		HttpUtil.createServer(8888)
				.addAction("/", (req, res)->{
					res.write("Hello Hutool Server");
				})
				.start();
	}
}
