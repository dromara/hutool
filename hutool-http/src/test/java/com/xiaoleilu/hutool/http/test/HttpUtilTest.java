package com.xiaoleilu.hutool.http.test;

import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.lang.Console;
import com.xiaoleilu.hutool.util.CharsetUtil;

public class HttpUtilTest {
	
	@Test
	@Ignore
	public void getTest() {
		String result1 = HttpUtil.get("http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2", "GBK");
		Console.log(result1);
	}
	
	@Test
	public void decodeParamsTest(){
		String paramsStr = "uuuu=0&a=b&c=%3F%23%40!%24%25%5E%26%3Ddsssss555555";
		Map<String, List<String>> map = HttpUtil.decodeParams(paramsStr, CharsetUtil.UTF_8);
		Assert.assertEquals("0", map.get("uuuu").get(0));
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("?#@!$%^&=dsssss555555", map.get("c").get(0));
	}
	
	@Test
	public void toParamsTest(){
		String paramsStr = "uuuu=0&a=b&c=3Ddsssss555555";
		Map<String, List<String>> map = HttpUtil.decodeParams(paramsStr, CharsetUtil.UTF_8);
		
		String encodedParams = HttpUtil.toParams((Map<String, List<String>>)map);
		Assert.assertEquals(paramsStr, encodedParams);
	}
}
