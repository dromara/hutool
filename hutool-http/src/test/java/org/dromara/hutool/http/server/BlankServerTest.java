package org.dromara.hutool.http.server;

import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.HttpUtil;

public class BlankServerTest {
	public static void main(final String[] args) {
		HttpUtil.createServer(8888)
				.addAction("/", (req, res)-> res.write("Hello Hutool Server", ContentType.JSON.getValue()))
				.start();
	}
}
