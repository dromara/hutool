package org.dromara.hutool.http.server;

import org.dromara.hutool.http.HttpUtil;

public class DocServerTest {

	public static void main(final String[] args) {
		HttpUtil.createServer(80)
				// 设置默认根目录，
				.setRoot("D:\\workspace\\site\\hutool-site")
				.start();
	}
}
