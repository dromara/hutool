package com.xiaoleilu.hutool.http.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;
import com.xiaoleilu.hutool.util.ReUtil;

public class HttpUtilTest {

	@Test
	@Ignore
	public void getTest() {
		String result1 = HttpUtil.get("http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2", "GBK");
		Console.log(result1);
	}

	@Test
	@Ignore
	public void getTest2() {
		// 测试url中带有空格的情况
		String result1 = HttpUtil.get("http://122.152.198.206:5000/kf?abc= d");
		Console.log(result1);
	}

	@Test
	@Ignore
	public void get12306Test() {
		String result = HttpUtil.get("https://kyfw.12306.cn/otn/");
		Console.log(result);
	}

	@Test
	@Ignore
	public void downloadStringTest() {
		String url = "https://www.baidu.com";
		// 从远程直接读取字符串，需要自定义编码，直接调用JDK方法
		String content2 = HttpUtil.downloadString(url, CharsetUtil.UTF_8);
		Console.log(content2);
	}

	@Test
	@Ignore
	public void oschinaTest() {
		// 请求列表页
		String listContent = HttpUtil.get("http://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=2");
		// 使用正则获取所有标题
		List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
		for (String title : titles) {
			// 打印标题
			Console.log(title);
		}
	}

	@Test
	public void decodeParamsTest() {
		String paramsStr = "uuuu=0&a=b&c=%3F%23%40!%24%25%5E%26%3Ddsssss555555";
		Map<String, List<String>> map = HttpUtil.decodeParams(paramsStr, CharsetUtil.UTF_8);
		Assert.assertEquals("0", map.get("uuuu").get(0));
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("?#@!$%^&=dsssss555555", map.get("c").get(0));
	}

	@Test
	public void toParamsTest() {
		String paramsStr = "uuuu=0&a=b&c=3Ddsssss555555";
		Map<String, List<String>> map = HttpUtil.decodeParams(paramsStr, CharsetUtil.UTF_8);

		String encodedParams = HttpUtil.toParams((Map<String, List<String>>) map);
		Assert.assertEquals(paramsStr, encodedParams);
	}
}
