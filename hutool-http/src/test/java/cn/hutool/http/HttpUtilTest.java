package cn.hutool.http;

import cn.hutool.core.io.file.FileUtil;
import cn.hutool.core.lang.Console;
import cn.hutool.core.regex.ReUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.client.Request;
import cn.hutool.http.meta.Header;
import cn.hutool.http.meta.Method;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("resource")
public class HttpUtilTest {

	@Test
	public void isHttpTest(){
		Assert.assertTrue(HttpUtil.isHttp("Http://aaa.bbb"));
		Assert.assertTrue(HttpUtil.isHttp("HTTP://aaa.bbb"));
		Assert.assertFalse(HttpUtil.isHttp("FTP://aaa.bbb"));
	}

	@Test
	public void isHttpsTest(){
		Assert.assertTrue(HttpUtil.isHttps("Https://aaa.bbb"));
		Assert.assertTrue(HttpUtil.isHttps("HTTPS://aaa.bbb"));
		Assert.assertTrue(HttpUtil.isHttps("https://aaa.bbb"));
		Assert.assertFalse(HttpUtil.isHttps("ftp://aaa.bbb"));
	}

	@Test
	@Ignore
	public void postTest2() {
		// 某些接口对Accept头有特殊要求，此处自定义头
		final String result = HttpUtil.send(Request
						.of("http://cmp.ishanghome.com/cmp/v1/community/queryClusterCommunity")
						.header(Header.ACCEPT, "*/*"))
				.bodyStr();
		Console.log(result);
	}

	@Test
	@Ignore
	public void getTest() {
		final String result1 = HttpUtil.get("http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2", CharsetUtil.GBK);
		Console.log(result1);
	}

	@Test
	@Ignore
	public void getTest2() {
		// 此链接较为特殊，User-Agent去掉后进入一个JS跳转页面，如果设置了，需要开启302跳转
		// 自定义的默认header无效
		final String result = Request
				.of("https://graph.qq.com/oauth2.0/authorize?response_type=code&client_id=101457313&redirect_uri=http%3A%2F%2Fwww.benmovip.com%2Fpay-cloud%2Fqqlogin%2FgetCode&state=ok")
				.header(Header.USER_AGENT, null).send().bodyStr();
		Console.log(result);
	}

	@Test
	@Ignore
	public void getTest3() {
		// 测试url中带有空格的情况
		final String result1 = HttpUtil.get("http://hutool.cn:5000/kf?abc= d");
		Console.log(result1);
	}

	@Test
	@Ignore
	public void getTest4() {
		// 测试url中带有空格的情况
		final byte[] str = Request.of("http://img01.fs.yiban.cn/mobile/2D0Y71").send().bodyBytes();

		FileUtil.writeBytes(str, "f:/test/2D.jpg");
		Console.log(str);
	}

	@Test
	@Ignore
	public void get12306Test() {
		HttpUtil.send(Request.of("https://kyfw.12306.cn/otn/").setMaxRedirectCount(2))
				.then(response -> Console.log(response.bodyStr()));
	}

	@Test
	@Ignore
	public void oschinaTest() {
		// 请求列表页
		String listContent = HttpUtil.get("https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=2");
		// 使用正则获取所有标题
		final List<String> titles = ReUtil.findAll("<span class=\"text-ellipsis\">(.*?)</span>", listContent, 1);
		for (final String title : titles) {
			// 打印标题
			Console.log(title);
		}

		// 请求下一页，检查Cookie是否复用
		listContent = HttpUtil.get("https://www.oschina.net/action/ajax/get_more_news_list?newsType=&p=3");
		Console.log(listContent);
	}

	@Test
	//@Ignore
	public void patchTest() {
		// 验证patch请求是否可用
		final String body = HttpUtil.send(Request.of("https://hutool.cn").method(Method.PATCH)).bodyStr();
		Console.log(body);
	}

	@Test
	public void urlWithFormTest() {
		final Map<String, Object> param = new LinkedHashMap<>();
		param.put("AccessKeyId", "123");
		param.put("Action", "DescribeDomainRecords");
		param.put("Format", "date");
		param.put("DomainName", "lesper.cn"); // 域名地址
		param.put("SignatureMethod", "POST");
		param.put("SignatureNonce", "123");
		param.put("SignatureVersion", "4.3.1");
		param.put("Timestamp", 123432453);
		param.put("Version", "1.0");

		String urlWithForm = HttpUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.UTF_8, false);
		Assert.assertEquals(
				"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
				urlWithForm);

		urlWithForm = HttpUtil.urlWithForm("http://api.hutool.cn/login?type=aaa", param, CharsetUtil.UTF_8, false);
		Assert.assertEquals(
				"http://api.hutool.cn/login?type=aaa&AccessKeyId=123&Action=DescribeDomainRecords&Format=date&DomainName=lesper.cn&SignatureMethod=POST&SignatureNonce=123&SignatureVersion=4.3.1&Timestamp=123432453&Version=1.0",
				urlWithForm);
	}

	@Test
	@Ignore
	public void getWeixinTest(){
		// 测试特殊URL，即URL中有&amp;情况是否请求正常
		final String url = "https://mp.weixin.qq.com/s?__biz=MzI5NjkyNTIxMg==&amp;mid=100000465&amp;idx=1&amp;sn=1044c0d19723f74f04f4c1da34eefa35&amp;chksm=6cbda3a25bca2ab4516410db6ce6e125badaac2f8c5548ea6e18eab6dc3c5422cb8cbe1095f7";
		final String s = HttpUtil.get(url);
		Console.log(s);
	}

	@Test
	@Ignore
	public void getNocovTest(){
		final String url = "https://qiniu.nocov.cn/medical-manage%2Ftest%2FBANNER_IMG%2F444004467954556928%2F1595215173047icon.png~imgReduce?e=1597081986&token=V2lJYVgQgAv_sbypfEZ0qpKs6TzD1q5JIDVr0Tw8:89cbBkLLwEc9JsMoCLkAEOu820E=";
		final String s = HttpUtil.get(url);
		Console.log(s);
	}

	@Test
	@Ignore
	public void sinajsTest(){
		final String s = HttpUtil.get("http://hq.sinajs.cn/list=sh600519");
		Console.log(s);
	}

	@Test
	@Ignore
	public void acplayTest(){
		final String body = HttpUtil.send(Request.of("https://api.acplay.net/api/v2/bangumi/9541")).bodyStr();
		Console.log(body);
	}

	@Test
	@Ignore
	public void getPicTest(){
		HttpGlobalConfig.setDecodeUrl(false);
		final String url = "https://p3-sign.douyinpic.com/tos-cn-i-0813/f41afb2e79a94dcf80970affb9a69415~noop.webp?x-expires=1647738000&x-signature=%2Br1ekUCGjXiu50Y%2Bk0MO4ovulK8%3D&from=4257465056&s=PackSourceEnum_DOUYIN_REFLOW&se=false&sh=&sc=&l=2022021809224601020810013524310DD3&biz_tag=aweme_images";

		final String body = HttpUtil.send(Request.of(url)).bodyStr();
		Console.log(body);
	}
}
