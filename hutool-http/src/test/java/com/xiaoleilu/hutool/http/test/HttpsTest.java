package com.xiaoleilu.hutool.http.test;

import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.http.HttpRequest;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Console;

/**
 * Https单元测试
 * @author looly
 *
 */
public class HttpsTest {
	
	@Test
	@Ignore
	public void getTest() {
		String body = HttpRequest.get("https://www.gjifa.com/pc/")
				.execute().body();
		Console.log(body);
	}
	
	@Test
	@Ignore
	public void get12306Test() {
		String result = HttpUtil.get("https://kyfw.12306.cn/otn/");
		Console.log(result);
	}
}
