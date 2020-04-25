package cn.hutool.core.net;

import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

public class UrlBuilderTest {

	@Test
	public void buildTest() {
		String buildUrl = UrlBuilder.create().setHost("www.baidu.com").build();
		Assert.assertEquals("http://www.baidu.com/", buildUrl);
	}

	@Test
	public void testHost() {
		String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.baidu.com").build();
		Assert.assertEquals("https://www.baidu.com/", buildUrl);
	}

	@Test
	public void testHostPort() {
		String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.baidu.com")
				.setPort(8080)
				.build();
		Assert.assertEquals("https://www.baidu.com:8080/", buildUrl);
	}

	@Test
	public void testPathAndQuery() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.baidu.com")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "test")
				.build();

		Assert.assertEquals("https://www.baidu.com/aaa/bbb?ie=UTF-8&wd=test", buildUrl);
	}

	@Test
	public void testQueryWithChinese() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.baidu.com")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "测试")
				.build();

		Assert.assertEquals("https://www.baidu.com/aaa/bbb?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testMultiQueryWithChinese() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.baidu.com")
				.addPath("/s")
				.addQuery("ie", "UTF-8")
				.addQuery("ie", "GBK")
				.addQuery("wd", "测试")
				.build();

		Assert.assertEquals("https://www.baidu.com/s?ie=UTF-8&ie=GBK&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testFragment() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.baidu.com")
				.setFragment("abc").build();
		Assert.assertEquals("https://www.baidu.com/#abc", buildUrl);
	}

	@Test
	public void testChineseFragment() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.baidu.com")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.baidu.com/#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPath() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.baidu.com")
				.addPath("/s")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.baidu.com/s#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPathAndQuery() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.baidu.com")
				.addPath("/s")
				.addQuery("wd", "test")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.baidu.com/s?wd=test#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void ofTest() {
		final UrlBuilder builder = UrlBuilder.of("http://www.baidu.com/aaa/bbb/?a=1&b=2#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.baidu.com", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("1", builder.getQuery().get("a"));
		Assert.assertEquals("2", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofWithChineseTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp("www.baidu.com/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.baidu.com", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("张三", builder.getQuery().get("a"));
		Assert.assertEquals("李四", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofWithBlankTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.baidu.com/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.baidu.com", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("张三", builder.getQuery().get("a"));
		Assert.assertEquals("李四", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofSpecialTest() {
		//测试不规范的或者无需解码的字符串是否成功解码
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.baidu.com/aaa/bbb/?a=张三&b=%%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.baidu.com", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("张三", builder.getQuery().get("a"));
		Assert.assertEquals("%四", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void weixinUrlTest(){
		String urlStr = "https://mp.weixin.qq.com/s?" +
				"__biz=MzI5NjkyNTIxMg==" +
				"&amp;mid=100000465" +
				"&amp;idx=1" +
				"&amp;sn=1044c0d19723f74f04f4c1da34eefa35" +
				"&amp;chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7";
		final UrlBuilder builder = UrlBuilder.ofHttp(urlStr, CharsetUtil.CHARSET_UTF_8);
		// 原URL中的&amp;替换为&，value中的=被编码为%3D
		Assert.assertEquals("https://mp.weixin.qq.com/s?" +
				"__biz=MzI5NjkyNTIxMg%3D%3D" +
				"&mid=100000465&idx=1" +
				"&sn=1044c0d19723f74f04f4c1da34eefa35" +
				"&chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7",
				builder.toString());
	}
}
