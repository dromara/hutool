package cn.hutool.http.test;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HttpUtilTest {

	@Test
	@Ignore
	public void postTest() {
		String result = HttpUtil.createPost("api.uhaozu.com/goods/description/1120448506").charset(CharsetUtil.UTF_8).execute().body();
		Console.log(result);
	}

	@Test
	@Ignore
	public void postTest2() {
		// 某些接口对Accept头有特殊要求，此处自定义头
		String result = HttpUtil
				.createPost("http://cmp.ishanghome.com/cmp/v1/community/queryClusterCommunity")
				.header(Header.ACCEPT, "*/*")
				.execute()
				.body();
		Console.log(result);
	}

	@Test
	@Ignore
	public void getTest() {
		String result1 = HttpUtil.get("http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2", CharsetUtil.CHARSET_GBK);
		Console.log(result1);
	}

	@Test
	@Ignore
	public void getTest2() {
		// 自定义的默认header无效
		String result = HttpRequest
				.get("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101457313&redirect_uri=http%3A%2F%2Fwww.benmovip.com%2Fpay-cloud%2Fqqlogin%2FgetCode&state=ok")
				.removeHeader(Header.USER_AGENT).execute().body();
		Console.log(result);
	}

	@Test
	@Ignore
	public void getTest3() {
		// 测试url中带有空格的情况
		String result1 = HttpUtil.get("http://122.152.198.206:5000/kf?abc= d");
		Console.log(result1);
	}

	@Test
	@Ignore
	public void getTest4() {
		// 测试url中带有空格的情况
		byte[] str = HttpRequest.get("http://img01.fs.yiban.cn/mobile/2D0Y71").execute().bodyBytes();

		FileUtil.writeBytes(str, "f:/test/2D.jpg");
		Console.log(str);
	}

	@Test
	@Ignore
	public void getTest5() {
		String url2 = "http://storage.chancecloud.com.cn/20200413_%E7%B2%A4B12313_386.pdf";
		ByteArrayOutputStream os2 = new ByteArrayOutputStream();
		HttpUtil.download(url2, os2, false);

		url2 = "http://storage.chancecloud.com.cn/20200413_粤B12313_386.pdf";
		HttpUtil.download(url2, os2, false);
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
		String listContent = HttpUtil.get("https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=2");
		// 使用正则获取所有标题
		List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
		for (String title : titles) {
			// 打印标题
			Console.log(title);
		}

		// 请求下一页，检查Cookie是否复用
		listContent = HttpUtil.get("https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=3");
		Console.log(listContent);
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
	public void decodeParamMapTest() {
		// 参数值存在分界标记等号时
		Map<String, String> paramMap = HttpUtil.decodeParamMap("https://www.xxx.com/api.action?aa=123&f_token=NzBkMjQxNDM1MDVlMDliZTk1OTU3ZDI1OTI0NTBiOWQ=", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("123",paramMap.get("aa"));
		Assert.assertEquals("NzBkMjQxNDM1MDVlMDliZTk1OTU3ZDI1OTI0NTBiOWQ=",paramMap.get("f_token"));
	}

	@Test
	public void toParamsTest() {
		String paramsStr = "uuuu=0&a=b&c=3Ddsssss555555";
		Map<String, List<String>> map = HttpUtil.decodeParams(paramsStr, CharsetUtil.UTF_8);

		String encodedParams = HttpUtil.toParams(map);
		Assert.assertEquals(paramsStr, encodedParams);
	}

	@Test
	public void encodeParamTest() {
		// ?单独存在去除之，&单位位于末尾去除之
		String paramsStr = "?a=b&c=d&";
		String encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=b&c=d", encode);

		// url不参与转码
		paramsStr = "http://www.abc.dd?a=b&c=d&";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("http://www.abc.dd?a=b&c=d", encode);

		// b=b中的=被当作值的一部分，不做encode
		paramsStr = "a=b=b&c=d&";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=b=b&c=d", encode);

		// =d的情况被处理为key为空
		paramsStr = "a=bbb&c=d&=d";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=bbb&c=d&=d", encode);

		// d=的情况被处理为value为空
		paramsStr = "a=bbb&c=d&d=";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=bbb&c=d&d=", encode);

		// 多个&&被处理为单个，相当于空条件
		paramsStr = "a=bbb&c=d&&&d=";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=bbb&c=d&d=", encode);

		// &d&相当于只有键，无值得情况
		paramsStr = "a=bbb&c=d&d&";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=bbb&c=d&d=", encode);

		// 中文的键和值被编码
		paramsStr = "a=bbb&c=你好&哈喽&";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("a=bbb&c=%E4%BD%A0%E5%A5%BD&%E5%93%88%E5%96%BD=", encode);

		// URL原样输出
		paramsStr = "https://www.hutool.cn/";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals(paramsStr, encode);

		// URL原样输出
		paramsStr = "https://www.hutool.cn/?";
		encode = HttpUtil.encodeParams(paramsStr, CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("https://www.hutool.cn/", encode);
	}

	@Test
	public void decodeParamTest() {
		// 开头的？被去除
		String a = "?a=b&c=d&";
		Map<String, List<String>> map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));

		// =e被当作空为key，e为value
		a = "?a=b&c=d&=e";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));
		Assert.assertEquals("e", map.get("").get(0));

		// 多余的&去除
		a = "?a=b&c=d&=e&&&&";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));
		Assert.assertEquals("e", map.get("").get(0));

		// 值为空
		a = "?a=b&c=d&e=";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));
		Assert.assertEquals("", map.get("e").get(0));

		// &=被作为键和值都为空
		a = "a=b&c=d&=";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));
		Assert.assertEquals("", map.get("").get(0));

		// &e&这类单独的字符串被当作key
		a = "a=b&c=d&e&";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("b", map.get("a").get(0));
		Assert.assertEquals("d", map.get("c").get(0));
		Assert.assertNull(map.get("e").get(0));
		Assert.assertNull(map.get("").get(0));

		// 被编码的键和值被还原
		a = "a=bbb&c=%E4%BD%A0%E5%A5%BD&%E5%93%88%E5%96%BD=";
		map = HttpUtil.decodeParams(a, CharsetUtil.UTF_8);
		Assert.assertEquals("bbb", map.get("a").get(0));
		Assert.assertEquals("你好", map.get("c").get(0));
		Assert.assertEquals("", map.get("哈喽").get(0));
	}

	@Test
	@Ignore
	public void patchTest() {
		String body = HttpRequest.patch("https://www.baidu.com").execute().body();
		Console.log(body);
	}

	@Test
	public void urlWithFormTest() {
		Map<String, Object> param = new LinkedHashMap<>();
		param.put("AccessKeyId", "123");
		param.put("Action", "DescribeDomainRecords");
		param.put("Format", "date");
		param.put("DomainName", "lesper.cn"); // 域名地址
		param.put("SignatureMethod", "POST");
		param.put("SignatureNonce", "123");
		param.put("SignatureVersion", "4.3.1");
		param.put("Timestamp", 123432453);
		param.put("Version", "1.0");

		String urlWithForm = HttpUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.CHARSET_UTF_8, false);
		Assert.assertEquals(
				"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
				urlWithForm);

		urlWithForm = HttpUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.CHARSET_UTF_8, false);
		Assert.assertEquals(
				"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
				urlWithForm);
	}

	@Test
	public void getCharsetTest() {
		String charsetName = ReUtil.get(HttpUtil.CHARSET_PATTERN, "Charset=UTF-8;fq=0.9", 1);
		Assert.assertEquals("UTF-8", charsetName);

		charsetName = ReUtil.get(HttpUtil.META_CHARSET_PATTERN, "<meta charset=utf-8", 1);
		Assert.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HttpUtil.META_CHARSET_PATTERN, "<meta charset='utf-8'", 1);
		Assert.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HttpUtil.META_CHARSET_PATTERN, "<meta charset=\"utf-8\"", 1);
		Assert.assertEquals("utf-8", charsetName);
		charsetName = ReUtil.get(HttpUtil.META_CHARSET_PATTERN, "<meta charset = \"utf-8\"", 1);
		Assert.assertEquals("utf-8", charsetName);
	}

	@Test
	public void normalizeParamsTest() {
		String encodeResult = HttpUtil.normalizeParams("参数", CharsetUtil.CHARSET_UTF_8);
		Assert.assertEquals("%E5%8F%82%E6%95%B0", encodeResult);
	}
	
	@Test
	public void getMimeTypeTest() {
		String mimeType = HttpUtil.getMimeType("aaa.aaa");
		Assert.assertNull(mimeType);
	}

	@Test
	@Ignore
	public void getWeixinTest(){
		// 测试特殊URL，即URL中有&amp;情况是否请求正常
		String url = "https://mp.weixin.qq.com/s?__biz=MzI5NjkyNTIxMg==&amp;mid=100000465&amp;idx=1&amp;sn=1044c0d19723f74f04f4c1da34eefa35&amp;chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7";
		final String s = HttpUtil.get(url);
		Console.log(s);
	}

	@Test
	@Ignore
	public void getNocovTest(){
		String url = "https://qiniu.nocov.cn/medical-manage%2Ftest%2FBANNER_IMG%2F444004467954556928%2F1595215173047icon.png~imgReduce?e=1597081986&token=V2lJYVgQgAv_sbypfEZ0qpKs6TzD1q5JIDVr0Tw8:89cbBkLLwEc9JsMoCLkAEOu820E=";
		final String s = HttpUtil.get(url);
		Console.log(s);
	}
}
