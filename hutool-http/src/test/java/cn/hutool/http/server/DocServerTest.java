package cn.hutool.http.server;

import cn.hutool.core.swing.DesktopUtil;
import cn.hutool.http.HttpUtil;

public class DocServerTest {

	public static void main(String[] args) {
		HttpUtil.createServer(80)
				// 设置默认根目录，
				.setRoot("D:\\workspace\\site\\hutool-site")
				// 返回JSON数据测试
				.start();

		DesktopUtil.browse("http://localhost/");
	}
}
