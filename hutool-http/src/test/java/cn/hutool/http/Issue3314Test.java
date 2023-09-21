package cn.hutool.http;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import org.junit.Ignore;
import org.junit.Test;

public class Issue3314Test {
	@Test
	@Ignore
	public void postTest() {
		String url = "https://hutool.cn/test/getList";
		final String body = HttpRequest.get(url)
			.setRest(true)
			.body(FileUtil.readBytes("d:/test/3314.xlsx"))
			.execute()
			.body();

		Console.log(body);
	}
}
