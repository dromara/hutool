package org.dromara.hutool.core.net;

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class UrlBuilderTest {

	@Test
	public void buildTest() {
		final UrlBuilder builder = UrlBuilder.of();
		final String buildUrl = builder.setHost("www.hutool.cn").build();

		Assertions.assertEquals("http://www.hutool.cn/", buildUrl);
		Assertions.assertEquals(80, builder.getPortWithDefault());
	}

	@Test
	public void buildWithoutSlashTest() {
		// https://github.com/dromara/hutool/issues/2459
		String buildUrl = UrlBuilder.of().setScheme("http").setHost("192.168.1.1").setPort(8080).setWithEndTag(false).build();
		Assertions.assertEquals("http://192.168.1.1:8080", buildUrl);

		final UrlBuilder urlBuilder = UrlBuilder.of();
		buildUrl = urlBuilder.setScheme("http").setHost("192.168.1.1").setPort(8080).addQuery("url", "http://192.168.1.1/test/1")
				.setWithEndTag(false).build();
		Assertions.assertEquals("http://192.168.1.1:8080?url=http://192.168.1.1/test/1", buildUrl);
		Assertions.assertEquals(8080, urlBuilder.getPortWithDefault());
	}

	@Test
	public void buildTest2() {
		// path中的+不做处理
		final String buildUrl = UrlBuilder.ofHttp("http://www.hutool.cn/+8618888888888", CharsetUtil.UTF_8).build();
		Assertions.assertEquals("http://www.hutool.cn/+8618888888888", buildUrl);
	}

	@Test
	public void testHost() {
		final String buildUrl = UrlBuilder.of()
				.setScheme("https")
				.setHost("www.hutool.cn").build();
		Assertions.assertEquals("https://www.hutool.cn/", buildUrl);
	}

	@Test
	public void testHostPort() {
		final String buildUrl = UrlBuilder.of()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setPort(8080)
				.build();
		Assertions.assertEquals("https://www.hutool.cn:8080/", buildUrl);
	}

	@Test
	public void testPathAndQuery() {
		final String buildUrl = UrlBuilder.of()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "test")
				.build();

		Assertions.assertEquals("https://www.hutool.cn/aaa/bbb?ie=UTF-8&wd=test", buildUrl);
	}

	@Test
	public void testQueryWithChinese() {
		final String buildUrl = UrlBuilder.of()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/aaa").addPath("bbb")
				.addQuery("ie", "UTF-8")
				.addQuery("wd", "测试")
				.build();

		Assertions.assertEquals("https://www.hutool.cn/aaa/bbb?ie=UTF-8&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testMultiQueryWithChinese() {
		final String buildUrl = UrlBuilder.of()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.addQuery("ie", "UTF-8")
				.addQuery("ie", "GBK")
				.addQuery("wd", "测试")
				.build();

		Assertions.assertEquals("https://www.hutool.cn/s?ie=UTF-8&ie=GBK&wd=%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testFragment() {
		final String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setFragment("abc").build();
		Assertions.assertEquals("https://www.hutool.cn/#abc", buildUrl);
	}

	@Test
	public void testChineseFragment() {
		final String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.setFragment("测试").build();
		Assertions.assertEquals("https://www.hutool.cn/#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPath() {
		final String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.setFragment("测试").build();
		Assertions.assertEquals("https://www.hutool.cn/s#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void testChineseFragmentWithPathAndQuery() {
		final String buildUrl = new UrlBuilder()
				.setScheme("https")
				.setHost("www.hutool.cn")
				.addPath("/s")
				.addQuery("wd", "test")
				.setFragment("测试").build();
		Assertions.assertEquals("https://www.hutool.cn/s?wd=test#%E6%B5%8B%E8%AF%95", buildUrl);
	}

	@Test
	public void ofTest() {
		final UrlBuilder builder = UrlBuilder.of("http://www.hutool.cn/aaa/bbb/?a=1&b=2#frag1", CharsetUtil.UTF_8);
		Assertions.assertEquals("http", builder.getScheme());
		Assertions.assertEquals("www.hutool.cn", builder.getHost());

		Assertions.assertEquals("aaa", builder.getPath().getSegment(0));
		Assertions.assertEquals("bbb", builder.getPath().getSegment(1));

		Assertions.assertEquals("1", builder.getQuery().get("a"));
		Assertions.assertEquals("2", builder.getQuery().get("b"));

		Assertions.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofNullQueryTest() {
		final UrlBuilder builder = UrlBuilder.of("http://www.hutool.cn/aaa/bbb", CharsetUtil.UTF_8);
		Assertions.assertNotNull(builder.getQuery());
		Assertions.assertNull(builder.getQuery().get("a"));
	}

	@Test
	public void ofWithChineseTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp("www.hutool.cn/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.UTF_8);
		Assertions.assertEquals("http", builder.getScheme());
		Assertions.assertEquals("www.hutool.cn", builder.getHost());

		Assertions.assertEquals("aaa", builder.getPath().getSegment(0));
		Assertions.assertEquals("bbb", builder.getPath().getSegment(1));

		Assertions.assertEquals("张三", builder.getQuery().get("a"));
		Assertions.assertEquals("李四", builder.getQuery().get("b"));

		Assertions.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofWithBlankTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.hutool.cn/aaa/bbb/?a=张三&b=%e6%9d%8e%e5%9b%9b#frag1", CharsetUtil.UTF_8);
		Assertions.assertEquals("http", builder.getScheme());
		Assertions.assertEquals("www.hutool.cn", builder.getHost());

		Assertions.assertEquals("aaa", builder.getPath().getSegment(0));
		Assertions.assertEquals("bbb", builder.getPath().getSegment(1));

		Assertions.assertEquals("张三", builder.getQuery().get("a"));
		Assertions.assertEquals("李四", builder.getQuery().get("b"));

		Assertions.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void ofSpecialTest() {
		//测试不规范的或者无需解码的字符串是否成功解码
		final UrlBuilder builder = UrlBuilder.ofHttp(" www.hutool.cn/aaa/bbb/?a=张三&b=%%e5%9b%9b#frag1", CharsetUtil.UTF_8);
		Assertions.assertEquals("http", builder.getScheme());
		Assertions.assertEquals("www.hutool.cn", builder.getHost());

		Assertions.assertEquals("aaa", builder.getPath().getSegment(0));
		Assertions.assertEquals("bbb", builder.getPath().getSegment(1));

		Assertions.assertEquals("张三", builder.getQuery().get("a"));
		Assertions.assertEquals("%四", builder.getQuery().get("b"));

		Assertions.assertEquals("frag1", builder.getFragment());
	}

	@Test
	public void weixinUrlTest() {
		final String urlStr = "https://mp.weixin.qq.com/s?" +
				"__biz=MzI5NjkyNTIxMg==" +
				"&amp;mid=100000465" +
				"&amp;idx=1" +
				"&amp;sn=1044c0d19723f74f04f4c1da34eefa35" +
				"&amp;chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7";
		final UrlBuilder builder = UrlBuilder.ofHttp(urlStr, CharsetUtil.UTF_8);
		// 原URL中的&amp;替换为&
		Assertions.assertEquals("https://mp.weixin.qq.com/s?" +
						"__biz=MzI5NjkyNTIxMg==" +
						"&mid=100000465&idx=1" +
						"&sn=1044c0d19723f74f04f4c1da34eefa35" +
						"&chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7",
				builder.toString());
	}

	@Test
	public void endWithSlashTest() {
		// 原URL中以/结尾，则这个规则需保留，issue#I1G44J@Gitee
		final String today = DateUtil.now().toString("yyyyMMdd");
		final String getWorkDayUrl = "https://tool.bitefu.net/jiari/?info=1&d=" + today;
		final UrlBuilder builder = UrlBuilder.ofHttp(getWorkDayUrl, CharsetUtil.UTF_8);
		Assertions.assertEquals(getWorkDayUrl, builder.toString());
	}

	@Test
	public void blankEncodeTest() {
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("http://a.com/aaa bbb.html", CharsetUtil.UTF_8);
		Assertions.assertEquals("http://a.com/aaa%20bbb.html", urlBuilder.toString());
	}

	@Test
	public void dotEncodeTest() {
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("http://xtbgyy.digitalgd.com.cn/ebus/../../..", CharsetUtil.UTF_8);
		Assertions.assertEquals("http://xtbgyy.digitalgd.com.cn/ebus/../../..", urlBuilder.toString());
	}

	@Test
	public void multiSlashTest() {
		//issue#I25MZL，某些URL中有多个斜杠，此为合法路径
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp("https://hutool.cn//file/test.jpg", CharsetUtil.UTF_8);
		Assertions.assertEquals("https://hutool.cn//file/test.jpg", urlBuilder.toString());
	}

	@Test
	public void toURITest() throws URISyntaxException {
		final String webUrl = "http://exmple.com/patha/pathb?a=123"; // 报错数据
		final UrlBuilder urlBuilder = UrlBuilder.of(webUrl, StandardCharsets.UTF_8);
		Assertions.assertEquals(new URI(webUrl), urlBuilder.toURI());
	}

	@Test
	public void testEncodeInQuery() {
		final String webUrl = "http://exmple.com/patha/pathb?a=123&b=4?6&c=789"; // b=4?6  参数中有未编码的？
		final UrlBuilder urlBuilder = UrlBuilder.of(webUrl, StandardCharsets.UTF_8);
		Assertions.assertEquals("a=123&b=4?6&c=789", urlBuilder.getQueryStr());
	}

	@Test
	public void encodePathTest() {
		// Path中的某些符号无需转义，比如=
		final String urlStr = "http://hq.sinajs.cn/list=sh600519";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp(urlStr, CharsetUtil.UTF_8);
		Assertions.assertEquals(urlStr, urlBuilder.toString());
	}

	@Test
	public void encodePathTest2() {
		// https://gitee.com/dromara/hutool/issues/I4RA42
		// Path中`:`在第一个segment需要转义，之后的不需要
		final String urlStr = "https://hutool.cn/aa/bb/Pre-K,Kindergarten,First,Second,Third,Fourth,Fifth/Page:3";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp(urlStr, CharsetUtil.UTF_8);
		Assertions.assertEquals(urlStr, urlBuilder.toString());
	}

	@Test
	public void gimg2Test() {
		final String url = "https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1114%2F0H320120Z3%2F200H3120Z3-6-1200.jpg&refer=http%3A%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621996490&t=8c384c2823ea453da15a1b9cd5183eea";
		final UrlBuilder urlBuilder = UrlBuilder.of(url);

		// PATH除了第一个path外，:是允许的
		final String url2 = "https://gimg2.baidu.com/image_search/src=http:%2F%2Fpic.jj20.com%2Fup%2Fallimg%2F1114%2F0H320120Z3%2F200H3120Z3-6-1200.jpg&refer=http:%2F%2Fpic.jj20.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1621996490&t=8c384c2823ea453da15a1b9cd5183eea";
		Assertions.assertEquals(url2, urlBuilder.toString());
	}

	@Test
	public void fragmentEncodeTest() {
		// https://gitee.com/dromara/hutool/issues/I49KAL
		// 见：https://stackoverflow.com/questions/26088849/url-fragment-allowed-characters
		final String url = "https://hutool.cn/docs/#/?id=简介";
		UrlBuilder urlBuilder = UrlBuilder.ofHttp(url);
		Assertions.assertEquals("https://hutool.cn/docs/#/?id=%E7%AE%80%E4%BB%8B", urlBuilder.toString());

		urlBuilder = UrlBuilder.ofHttp(urlBuilder.toString());
		Assertions.assertEquals(urlBuilder.toString(), urlBuilder.toString());
	}

	@Test
	public void slashEncodeTest() {
		// https://github.com/dromara/hutool/issues/1904
		// 在query中，"/"是不可转义字符
		// 见：https://www.rfc-editor.org/rfc/rfc3986.html#section-3.4
		final String url = "https://invoice.maycur.com/2b27a802-8423-4d41-86f5-63a6b259f61e.xlsx?download/2b27a802-8423-4d41-86f5-63a6b259f61e.xlsx&e=1630491088";
		final UrlBuilder urlBuilder = UrlBuilder.ofHttp(url);
		Assertions.assertEquals(url, urlBuilder.toString());
	}

	@Test
	public void addPathEncodeTest() {
		final String url = UrlBuilder.of()
				.setScheme("https")
				.setHost("domain.cn")
				.addPath("api")
				.addPath("xxx")
				.addPath("bbb")
				.build();

		Assertions.assertEquals("https://domain.cn/api/xxx/bbb", url);
	}

	@Test
	public void addPathEncodeTest2() {
		// https://github.com/dromara/hutool/issues/1912
		final String url = UrlBuilder.of()
				.setScheme("https")
				.setHost("domain.cn")
				.addPath("/api/xxx/bbb")
				.build();

		Assertions.assertEquals("https://domain.cn/api/xxx/bbb", url);
	}

	@Test
	public void percent2BTest() {
		final String url = "http://xxx.cn/a?Signature=3R013Bj9Uq4YeISzAs2iC%2BTVCL8%3D";
		final UrlBuilder of = UrlBuilder.ofHttpWithoutEncode(url);
		Assertions.assertEquals(url, of.toString());
	}

	@Test
	public void paramTest() {
		final String url = "http://ci.xiaohongshu.com/spectrum/c136c98aa2047babe25b994a26ffa7b492bd8058?imageMogr2/thumbnail/x800/format/jpg";
		final UrlBuilder builder = UrlBuilder.ofHttp(url);
		Assertions.assertEquals(url, builder.toString());
	}

	@Test
	public void fragmentTest() {
		// https://gitee.com/dromara/hutool/issues/I49KAL#note_8060874
		final String url = "https://www.hutool.cn/#/a/b?timestamp=1640391380204";
		final UrlBuilder builder = UrlBuilder.ofHttp(url);

		Assertions.assertEquals(url, builder.toString());
	}

	@Test
	public void fragmentAppendParamTest() {
		// https://gitee.com/dromara/hutool/issues/I49KAL#note_8060874
		final String url = "https://www.hutool.cn/#/a/b";
		final UrlBuilder builder = UrlBuilder.ofHttp(url);
		builder.setFragment(builder.getFragment() + "?timestamp=1640391380204");
		Assertions.assertEquals("https://www.hutool.cn/#/a/b?timestamp=1640391380204", builder.toString());
	}

	@Test
	public void paramWithPlusTest() {
		final String url = "http://127.0.0.1/?" +
				"Expires=1642734164&" +
				"security-token=CAIS+AF1q6Ft5B2yfSjIr5fYEeju1b1ggpPee2KGpjlgQtdfl43urjz2IHtKdXRvBu8Xs" +
				"/4wnmxX7f4YlqB6T55OSAmcNZEoPwKpT4zmMeT7oMWQweEurv" +
				"/MQBqyaXPS2MvVfJ+OLrf0ceusbFbpjzJ6xaCAGxypQ12iN+/m6" +
				"/Ngdc9FHHPPD1x8CcxROxFppeIDKHLVLozNCBPxhXfKB0ca0WgVy0EHsPnvm5DNs0uH1AKjkbRM9r6ceMb0M5NeW75kSMqw0eBMca7M7TVd8RAi9t0t1" +
				"/IVpGiY4YDAWQYLv0rda7DOltFiMkpla7MmXqlft+hzcgeQY0pc" +
				"/RqAAYRYVCBiyuzAexSiDiJX1VqWljg4jYp1sdyv3HpV3sXVcf6VH6AN9ot5YNTw4JNO0aNpLpLm93rRMrOKIOsve+OmNyZ4HS7qHQKt1qp7HY1A" +
				"/wGhJstkAoGQt+CHSMwVdIx3bVT1+ZYnJdM/oIQ/90afw4EEEQaRE51Z0rQC7z8d";
		final String build = UrlBuilder.of(url).build();
		Assertions.assertEquals(url, build);
	}

	@Test
	public void issueI4Z2ETTest() {
		// =是url参数值中的合法字符，但是某些URL强制编码了
		final String url = "http://dsl-fd.dslbuy.com/fssc/1647947565522.pdf?" +
				"Expires=1647949365" +
				"&OSSAccessKeyId=STS.NTZ9hvqPSLG8ENknz2YaByLKj" +
				"&Signature=oYUu26JufAyPY4PdzaOp1x4sr4Q%3D";

		final UrlBuilder urlBuilder = UrlBuilder.ofHttp(url, null);
		Assertions.assertEquals(url, urlBuilder.toString());
	}

	@Test
	public void issue2215Test() {
		final String url = "https://hutool.cn/v1/104303371/messages:send";
		final String build = UrlBuilder.of(url).build();
		Assertions.assertEquals(url, build);
	}

	@Test
	public void issuesI4Z2ETTest() {
		final String url = "http://hutool.cn/2022/03/09/123.zip?Expires=1648704684&OSSAccessKeyId=LTAI4FncgaVtwZGBnYHHi8ox&Signature=%2BK%2B%3D";
		final String build = UrlBuilder.of(url, null).build();
		Assertions.assertEquals(url, build);
	}

	@Test
	public void issueI50NHQTest() {
		final String url = "http://127.0.0.1/devicerecord/list";
		final HashMap<String, Object> params = new LinkedHashMap<>();
		params.put("start", "2022-03-31 00:00:00");
		params.put("end", "2022-03-31 23:59:59");
		params.put("page", 1);
		params.put("limit", 10);

		final UrlBuilder builder = UrlBuilder.of(url);
		params.forEach(builder::addQuery);
		Assertions.assertEquals("http://127.0.0.1/devicerecord/list?start=2022-03-31%2000:00:00&end=2022-03-31%2023:59:59&page=1&limit=10", builder.toString());
	}

	@Test
	public void issue2243Test() {
		// https://github.com/dromara/hutool/issues/2243
		// 如果用户已经做了%编码，不应该重复编码
		final String url = "https://hutool.cn/v1.0?privateNum=%2B8616512884988";
		final String s = UrlBuilder.of(url, null).setCharset(CharsetUtil.UTF_8).toString();
		Assertions.assertEquals(url, s);
	}

	@Test
	public void issueI51T0VTest() {
		// &amp;自动转换为&
		final String url = "https://hutool.cn/a.mp3?Expires=1652423884&amp;key=JMv2rKNc7Pz&amp;sign=12zva00BpVqgZcX1wcb%2BrmN7H3E%3D";
		final UrlBuilder of = UrlBuilder.of(url, null);
		Assertions.assertEquals(url.replace("&amp;", "&"), of.toString());
	}

	@SuppressWarnings("ConstantConditions")
	@Test
	public void issues2503Test() throws URISyntaxException {
		final String duplicate = UrlBuilder.ofHttp("127.0.0.1:8080")
				.addQuery("param[0].field", "编码")
				.toURI()
				.toString();
		Assertions.assertEquals("http://127.0.0.1:8080?param%5B0%5D.field=%E7%BC%96%E7%A0%81", duplicate);

		final String normal = UrlBuilder.ofHttp("127.0.0.1:8080")
				.addQuery("param[0].field", "编码")
				.toURL()
				.toURI()
				.toString();
		Assertions.assertEquals(duplicate, normal);
	}

	@Test
	public void getAuthorityTest() {
		final UrlBuilder builder = UrlBuilder.ofHttp("127.0.0.1:8080")
				.addQuery("param[0].field", "编码");

		Assertions.assertEquals("127.0.0.1:8080", builder.getAuthority());
	}

	@Test
	public void addPathTest() {
		//https://gitee.com/dromara/hutool/issues/I5O4ML
		UrlBuilder.of().addPath("");
		UrlBuilder.of().addPath("/");
		UrlBuilder.of().addPath("//");
		UrlBuilder.of().addPath("//a");
	}

	@Test
	public void ofHttpTest() {
		UrlBuilder ofHttp = UrlBuilder.ofHttp("http://hutool.cn");
		Assertions.assertEquals("http://hutool.cn", ofHttp.toString());

		ofHttp = UrlBuilder.ofHttp("https://hutool.cn");
		Assertions.assertEquals("https://hutool.cn", ofHttp.toString());

		ofHttp = UrlBuilder.ofHttp("hutool.cn");
		Assertions.assertEquals("http://hutool.cn", ofHttp.toString());

		ofHttp = UrlBuilder.ofHttp("hutool.cn?old=http://aaa");
		Assertions.assertEquals("http://hutool.cn?old=http://aaa", ofHttp.toString());
	}
}
