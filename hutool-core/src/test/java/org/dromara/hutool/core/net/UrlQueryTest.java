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

package org.dromara.hutool.core.net;

import org.dromara.hutool.core.map.MapUtil;
import org.dromara.hutool.core.net.url.UrlUtil;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UrlQueryTest {

	@Test
	public void parseTest() {
		final String queryStr = "a=1&b=111==";
		final UrlQuery q = UrlQuery.of();
		final UrlQuery parse = q.parse(queryStr, Charset.defaultCharset());
		Assertions.assertEquals("111==", parse.get("b"));
		Assertions.assertEquals("a=1&b=111==", parse.toString());
	}

	@Test
	public void ofHttpWithoutEncodeTest() {
		// charset为null表示不做编码
		final String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttpWithoutEncode(url);
		final String queryStr = urlBuilder.getQueryStr();
		Assertions.assertEquals("imageMogr2/auto-orient/thumbnail/500&pid=259848", queryStr);
	}

	@Test
	public void parseTest2() {
		final String requestUrl = "http://192.168.1.1:8080/pc?=d52i5837i4ed=o39-ap9e19s5--=72e54*ll0lodl-f338868d2";
		final UrlQuery q = UrlQuery.of();
		final UrlQuery parse = q.parse(requestUrl, Charset.defaultCharset());
		Assertions.assertEquals("=d52i5837i4ed=o39-ap9e19s5--=72e54*ll0lodl-f338868d2", parse.toString());
	}

	@Test
	public void parseTest3() {
		// issue#1688@Github
		final String u = "https://www.baidu.com/proxy";
		final UrlQuery query = UrlQuery.of(UrlUtil.url(u).getQuery(), Charset.defaultCharset());
		Assertions.assertTrue(MapUtil.isEmpty(query.getQueryMap()));
	}

	@Test
	public void parseTest4() {
		// https://github.com/dromara/hutool/issues/1989
		final String queryStr = "imageMogr2/thumbnail/x800/format/jpg";
		final UrlQuery query = UrlQuery.of(queryStr, CharsetUtil.UTF_8);
		Assertions.assertEquals(queryStr, query.toString());
	}

	@Test
	public void buildWithMapTest() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("username", "SSM");
		map.put("password", "123456");
		String query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("username=SSM&password=123456", query);

		map = new TreeMap<>();
		map.put("username", "SSM");
		map.put("password", "123456");
		query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("password=123456&username=SSM", query);
	}

	@Test
	public void buildHasNullTest() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put(null, "SSM");
		map.put("password", "123456");
		String query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("password=123456", query);

		map = new TreeMap<>();
		map.put("username", "SSM");
		map.put("password", "");
		query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("password=&username=SSM", query);

		map = new TreeMap<>();
		map.put("username", "SSM");
		map.put("password", null);
		query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("password&username=SSM", query);
	}

	@Test
	public void buildSpecialTest() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("key1&", "SSM");
		map.put("key2", "123456&");
		String query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("key1%26=SSM&key2=123456%26", query);

		map = new TreeMap<>();
		map.put("username=", "SSM");
		map.put("password", "=");
		query = UrlUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assertions.assertEquals("password==&username%3D=SSM", query);
	}

	@Test
	public void plusTest() {
		// 根据RFC3986，在URL中，+是安全字符，即此符号不转义
		final String a = UrlQuery.of(MapUtil.of("a+b", "1+2")).build(CharsetUtil.UTF_8);
		Assertions.assertEquals("a+b=1+2", a);
	}

	@Test
	public void parsePlusTest() {
		// 根据RFC3986，在URL中，+是安全字符，即此符号不转义
		final String a = UrlQuery.of("a+b=1+2", CharsetUtil.UTF_8)
			.build(CharsetUtil.UTF_8);
		Assertions.assertEquals("a+b=1+2", a);
	}

	@Test
	public void spaceTest() {
		// 根据RFC3986，在URL中，空格编码为"%20"
		final String a = UrlQuery.of(MapUtil.of("a ", " ")).build(CharsetUtil.UTF_8);
		Assertions.assertEquals("a%20=%20", a);
	}

	@Test
	public void parsePercentTest() {
		final String queryStr = "a%2B=ccc";
		final UrlQuery query = UrlQuery.of(queryStr, null);
		Assertions.assertEquals(queryStr, query.toString());
	}

	@Test
	public void parsePercentTest2() {
		final String queryStr = "signature=%2Br1ekUCGjXiu50Y%2Bk0MO4ovulK8%3D";
		final UrlQuery query = UrlQuery.of(queryStr, null);
		Assertions.assertEquals(queryStr, query.toString());
	}

	@Test
	void issueI78PB1Test() {
		// 严格模式
		final UrlQuery query = UrlQuery.of(UrlQuery.EncodeMode.STRICT);
		query.add(":/?#[]@!$&'()*+,;= ", ":/?#[]@!$&'()*+,;= ");

		final String string = query.build(CharsetUtil.UTF_8);
		Assertions.assertEquals("%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2A%2B%2C%3B%3D%20=" +
			"%3A%2F%3F%23%5B%5D%40%21%24%26%27%28%29%2A%2B%2C%3B%3D%20", string);
	}
}
