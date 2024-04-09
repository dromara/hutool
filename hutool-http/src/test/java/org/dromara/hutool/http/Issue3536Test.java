package org.dromara.hutool.http;

import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.net.url.UrlQueryUtil;
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

	@Test
	public void toQueryTest() {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("redirect_uri", "https://api.hutool.cn/v1/test");
		paramMap.put("scope", "a,b,c你");

		final String params = UrlQueryUtil.toQuery(paramMap, CharsetUtil.UTF_8, UrlQuery.EncodeMode.STRICT);
		Assert.assertEquals("scope=a%2Cb%2Cc%E4%BD%A0&redirect_uri=https%3A%2F%2Fapi.hutool.cn%2Fv1%2Ftest", params);
	}
}
