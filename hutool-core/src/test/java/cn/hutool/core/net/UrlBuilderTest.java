package cn.hutool.core.net;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import org.junit.Assert;
import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;

public class UrlBuilderTest {

	@Test
	public void buildTest() {
		String buildUrl = UrlBuilder.create().setHost("www.hutool.cn").build();
		Assert.assertEquals("http://www.hutool.cn/", buildUrl);
	}

	@Test
	public void buildTest2() {
		// path中的+不做处理
		String buildUrl = UrlBuilder.ofHttp("http://www.hutool.cn/+8618888888888", CharsetUtil.CHARSET_UTF_8).build();
		Assert.assertEquals("http://www.hutool.cn/+8618888888888", buildUrl);
	}

	@Test
	public void testHost() {
		String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.hutool.cn").build();
		Assert.assertEquals("https://www.hutool.cn/", buildUrl);
	}

	@Test
	public void testHostPort() {
		String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setPort(8080)
				.build();
		Assert.assertEquals("https://www.hutool.cn:8080/", buildUrl);
	}

	@Test
	public void testPathAndQuery() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "test")
				.build();

		Assert.assertEquals("https://www.hutool.cn/aaa/bbb?ie=UTF-8&wd=test", buildUrl);
	}

	@Test
	public void testQueryWithChinese() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "测试")
				.build();

		Assert.assertEquals("https://www.hutool.cn/aaa/bbb?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testMultiQueryWithChinese() {
		final String buildUrl = UrlBuilder.create()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.addQuery("ie", "UTF-8")
				.addQuery("ie", "GBK")
				.addQuery("wd", "测试")
				.build();

		Assert.assertEquals("https://www.hutool.cn/s?ie=UTF-8&ie=GBK&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testFragment() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setFragment("abc").build();
		Assert.assertEquals("https://www.hutool.cn/#abc", buildUrl);
	}

	@Test
	public void testChineseFragment() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.hutool.cn/#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPath() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.hutool.cn/s#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPathAndQuery() {
		String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.addQuery("wd", "test")
				.setFragment("测试").build();
		Assert.assertEquals("https://www.hutool.cn/s?wd=test#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void ofTest() {
		final UrlBuilder builder = UrlBuilder.of("http://www.hutool.cn/aaa/bbb/?a=1&b=2#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.hutool.cn", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("1", builder.getQuery().get("a"));
		Assert.assertEquals("2", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofWithChineseTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp("www.hutool.cn/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.hutool.cn", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("张三", builder.getQuery().get("a"));
		Assert.assertEquals("李四", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofWithBlankTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.hutool.cn/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.hutool.cn", builder.getHost());

		Assert.assertEquals("aaa", builder.getPath().getSegment(0));
		Assert.assertEquals("bbb", builder.getPath().getSegment(1));

		Assert.assertEquals("张三", builder.getQuery().get("a"));
		Assert.assertEquals("李四", builder.getQuery().get("b"));

		Assert.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofSpecialTest() {
		//测试不规范的或者无需解码的字符串是否成功解码
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.hutool.cn/aaa/bbb/?a=张三&b=%%e5%9b%9b#frag1", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http", builder.getScheme());
		Assert.assertEquals("www.hutool.cn", builder.getHost());

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

	@Test
	public void endWithSlashTest(){
		// 原URL中以/结尾，则这个规则需保留，issue#I1G44J@Gitee
		final String today = DateUtil.date().toString("yyyyMMdd");
		final String getWorkDayUrl = "https://tool.bitefu.net/jiari/?info=1&d=" + today;
		final UrlBuilder builder = UrlBuilder.ofHttp(getWorkDayUrl, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(getWorkDayUrl, builder.toString());
	}

	@Test
	public void blankEncodeTest(){
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("http://a.com/aaa bbb.html", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http://a.com/aaa%20bbb.html", urlBuilder.toString());
	}

	@Test
	public void dotEncodeTest(){
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("http://xtbgyy.digitalgd.com.cn/ebus/../../..", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http://xtbgyy.digitalgd.com.cn/ebus/../../..", urlBuilder.toString());
	}

	@Test
	public void multiSlashTest(){
		//issue#I25MZL，某些URL中有多个斜杠，此为合法路径
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("https://hutool.cn//file/test.jpg", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("https://hutool.cn//file/test.jpg", urlBuilder.toString());
	}

	@Test
	public void toURITest() throws URISyntaxException {
		String webUrl = "http://exmple.com/patha/pathb?a=123"; // 报错数据
		final UrlBuilder urlBuilder = UrlBuilder.of(webUrl, StandardCharsets.UTF_8);
		Assert.assertEquals(new URI(webUrl), urlBuilder.toURI());
	}

	@Test
	public void testEncodeInQuery() {
		String webUrl = "http://exmple.com/patha/pathb?a=123&b=4?6&c=789"; // b=4?6  参数中有未编码的？
		final UrlBuilder urlBuilder = UrlBuilder.of(webUrl, StandardCharsets.UTF_8);
		Assert.assertEquals("a=123&b=4%3F6&c=789", urlBuilder.getQueryStr());
	}

	@Test
	public void encodePathTest(){
		// Path中的某些符号无需转义，比如=
		final String urlStr = "http://hq.sinajs.cn/list=sh600519";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp(urlStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(urlStr, urlBuilder.toString());
	}

	@Test
	public void gimg2Test(){
		String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1114%2F0H320120Z3%2F200H3120Z3-6-1200.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621996490&t=8c384c2823ea453da15a1b9cd5183eea";
		final UrlBuilder urlBuilder = UrlBuilder.of(url);


		Assert.assertEquals(url, urlBuilder.toString());
	}

	@Test
	public void fragmentEncodeTest(){
		// https://gitee.com/dromara/hutool/issues/I49KAL
		// 见：https://stackoverflow.com/questions/26088849/url-fragment-allowed-characters
		String url = "https://hutool.cn/docs/#/?id=简介";
		UrlBuilder urlBuilder = UrlBuilder.ofHttp(url);
		Assert.assertEquals("https://hutool.cn/docs/#/?id=%E7%AE%80%E4%BB%8B", urlBuilder.toString());

		urlBuilder = UrlBuilder.ofHttp(urlBuilder.toString());
		Assert.assertEquals(urlBuilder.toString(), urlBuilder.toString());
	}
}
