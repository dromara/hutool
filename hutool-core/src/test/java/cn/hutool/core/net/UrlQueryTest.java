package cn.hutool.core.net;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.net.url.UrlQuery;
import cn.hutool.core.util.URLUtil;
import org.junit.Assert;
import org.junit.Test;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class UrlQueryTest {

	@Test
	public void parseTest(){
		String queryStr = "a=1&b=111==";
		UrlQuery q = new UrlQuery();
		UrlQuery parse = q.parse(queryStr, Charset.defaultCharset());
		Assert.assertEquals("111==", parse.get("b"));
		Assert.assertEquals("a=1&b=111==", parse.toString());
	}

	@Test
	public void ofHttpWithoutEncodeTest(){
		// charset为null表示不做编码
		String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttpWithoutEncode(url);
		final String queryStr = urlBuilder.getQueryStr();
		Assert.assertEquals("imageMogr2/auto-orient/thumbnail/500&pid=259848", queryStr);
	}

	@Test
	public void parseTest2(){
		String requestUrl = "http://192.168.1.1:8080/pc?=d52i5837i4ed=o39-ap9e19s5--=72e54*ll0lodl-f338868d2";
		UrlQuery q = new UrlQuery();
		UrlQuery parse = q.parse(requestUrl, Charset.defaultCharset());
		Assert.assertEquals("=d52i5837i4ed=o39-ap9e19s5--=72e54*ll0lodl-f338868d2", parse.toString());
	}

	@Test
	public void parseTest3(){
		// issue#1688@Github
		String u = "https://www.baidu.com/proxy";
		final UrlQuery query = UrlQuery.of(u, Charset.defaultCharset());
		Assert.assertTrue(MapUtil.isEmpty(query.getQueryMap()));
	}

	@Test
	public void buildWithMapTest() {
		Map<String, String> map = new LinkedHashMap<>();
		map.put("username", "SSM");
		map.put("password", "123456");
		String query = URLUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assert.assertEquals("username=SSM&password=123456", query);

		map = new TreeMap<>();
		map.put("username", "SSM");
		map.put("password", "123456");
		query = URLUtil.buildQuery(map, StandardCharsets.UTF_8);
		Assert.assertEquals("password=123456&username=SSM", query);
	}
}
