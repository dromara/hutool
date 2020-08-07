package cn.hutool.http.server;

import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.http.ContentType;
import cn.hutool.http.HttpUtil;

public class BlankServerTest {
	public static void main(String[] args) {
		HttpUtil.createServer(8888)
				.addAction("/", (req, res)-> res.write("Hello Hutool Server", ContentType.JSON.getValue()))
				.start();

		DesktopUtil.browse("http://localhost:8888/");
	}
}
