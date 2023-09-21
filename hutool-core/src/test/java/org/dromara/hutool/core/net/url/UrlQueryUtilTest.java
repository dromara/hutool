/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.core.net.url;

import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

public class UrlQueryUtilTest {
	@Test
	public void decodeQueryTest() {
		final String paramsStr = "uuuu=0&a=b&c=%3F%23%40!%24%25%5E%26%3Ddsssss555555";
		final Map<String, List<String>> map = UrlQueryUtil.decodeQueryList(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("0", map.get("uuuu").get(0));
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("?#@!$%^&=dsssss555555", map.get("c").get(0));
	}

	@Test
	public void decodeQueryTest2() {
		// 参数值存在分界标记等号时
		final Map<String, String> paramMap = UrlQueryUtil.decodeQuery("https://www.xxx.com/api.action?aa=123&f_token=NzBkMjQxNDM1MDVlMDliZTk1OTU3ZDI1OTI0NTBiOWQ=", CharsetUtil.UTF_8);
		Assertions.assertEquals("123",paramMap.get("aa"));
		Assertions.assertEquals("NzBkMjQxNDM1MDVlMDliZTk1OTU3ZDI1OTI0NTBiOWQ=",paramMap.get("f_token"));
	}

	@Test
	public void toQueryTest() {
		final String paramsStr = "uuuu=0&a=b&c=3Ddsssss555555";
		final Map<String, List<String>> map = UrlQueryUtil.decodeQueryList(paramsStr, CharsetUtil.UTF_8);

		final String encodedParams = UrlQueryUtil.toQuery(map);
		Assertions.assertEquals(paramsStr, encodedParams);
	}

	@Test
	public void encodeParamTest() {
		// ?单独存在去除之，&单位位于末尾去除之
		String paramsStr = "?a=b&c=d&";
		String encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=b&c=d", encode);

		// url不参与转码
		paramsStr = "http://www.abc.dd?a=b&c=d&";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("http://www.abc.dd?a=b&c=d", encode);

		// b=b中的=被当作值的一部分，不做encode
		paramsStr = "a=b=b&c=d&";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=b=b&c=d", encode);

		// =d的情况被处理为key为空
		paramsStr = "a=bbb&c=d&=d";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=bbb&c=d&=d", encode);

		// d=的情况被处理为value为空
		paramsStr = "a=bbb&c=d&d=";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=bbb&c=d&d=", encode);

		// 多个&&被处理为单个，相当于空条件
		paramsStr = "a=bbb&c=d&&&d=";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=bbb&c=d&d=", encode);

		// &d&相当于只有键，无值得情况
		paramsStr = "a=bbb&c=d&d&";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=bbb&c=d&d=", encode);

		// 中文的键和值被编码
		paramsStr = "a=bbb&c=你好&哈喽&";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("a=bbb&c=%E4%BD%A0%E5%A5%BD&%E5%93%88%E5%96%BD=", encode);

		// URL原样输出
		paramsStr = "https://www.hutool.cn/";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals(paramsStr, encode);

		// URL原样输出
		paramsStr = "https://www.hutool.cn/?";
		encode = UrlQueryUtil.encodeQuery(paramsStr, CharsetUtil.UTF_8);
		Assertions.assertEquals("https://www.hutool.cn/", encode);
	}

	@Test
	public void decodeParamTest() {
		// 开头的？被去除
		String a = "?a=b&c=d&";
		Map<String, List<String>> map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));

		// =e被当作空为key，e为value
		a = "?a=b&c=d&=e";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));
		Assertions.assertEquals("e", map.get("").get(0));

		// 多余的&去除
		a = "?a=b&c=d&=e&&&&";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));
		Assertions.assertEquals("e", map.get("").get(0));

		// 值为空
		a = "?a=b&c=d&e=";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));
		Assertions.assertEquals("", map.get("e").get(0));

		// &=被作为键和值都为空
		a = "a=b&c=d&=";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));
		Assertions.assertEquals("", map.get("").get(0));

		// &e&这类单独的字符串被当作key
		a = "a=b&c=d&e&";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("b", map.get("a").get(0));
		Assertions.assertEquals("d", map.get("c").get(0));
		Assertions.assertNull(map.get("e").get(0));
		Assertions.assertNull(map.get("").get(0));

		// 被编码的键和值被还原
		a = "a=bbb&c=%E4%BD%A0%E5%A5%BD&%E5%93%88%E5%96%BD=";
		map = UrlQueryUtil.decodeQueryList(a, CharsetUtil.UTF_8);
		Assertions.assertEquals("bbb", map.get("a").get(0));
		Assertions.assertEquals("你好", map.get("c").get(0));
		Assertions.assertEquals("", map.get("哈喽").get(0));
	}

	@Test
	public void normalizeQueryTest() {
		final String encodeResult = UrlQueryUtil.normalizeQuery("参数", CharsetUtil.UTF_8);
		Assertions.assertEquals("%E5%8F%82%E6%95%B0", encodeResult);
	}

	@Test
	public void normalizeBlankQueryTest() {
		final String encodeResult = UrlQueryUtil.normalizeQuery("", CharsetUtil.UTF_8);
		Assertions.assertEquals("", encodeResult);
	}
}
