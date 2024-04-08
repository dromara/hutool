package org.dromara.hutool.http;

import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue3536Test {

	@Test
	public void urlWithFormUrlEncodedTest() {
		final String url = "https://hutool.cn/test";
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("redirect_uri", "https://api.hutool.cn/v1/test");
		paramMap.put("scope", "a,b,c你");

		final String s = HttpUtil.urlWithFormUrlEncoded(url, paramMap, CharsetUtil.UTF_8);
		Assert.assertEquals("https://hutool.cn/test?scope=a,b,c%E4%BD%A0&redirect_uri=https://api.hutool.cn/v1/test", s);
	}

}
