package cn.hutool.http.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import org.junit.Ignore;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * {@link HttpRequest}单元测试
 * 
 * @author Looly
 *
 */
public class HttpRequestTest {
	final String url = "http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2";

	@Test
	@Ignore
	public void getHttpsTest() {
		String body = HttpRequest.get("https://www.gjifa.com/pc/").execute().body();
		Console.log(body);
	}

	@Test
	@Ignore
	public void getCookiesTest() {
		// 检查在Connection关闭情况下Cookie是否可以正常获取
		HttpResponse res = HttpRequest.get("https://www.oschina.net/").execute();
		String body = res.body();
		Console.log(res.getCookies());
		Console.log(body);
	}

	@Test
	@Ignore
	public void toStringTest() {
		String url = "http://gc.ditu.aliyun.com/geocoding?ccc=你好";
		
		HttpRequest request = HttpRequest.get(url).body("a=乌海");
		Console.log(request.toString());
	}

	@Test
	@Ignore
	public void asyncHeadTest() {
		HttpResponse response = HttpRequest.head(url).execute();
		Map<String, List<String>> headers = response.headers();
		Console.log(headers);
		Console.log(response.body());
	}

	@Test
	@Ignore
	public void asyncGetTest() {
		TimeInterval timer = DateUtil.timer();
		HttpResponse body = HttpRequest.get(url).charset("GBK").executeAsync();
		long interval = timer.interval();
		timer.restart();
		Console.log(body.body());
		long interval2 = timer.interval();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Ignore
	public void syncGetTest() {
		TimeInterval timer = DateUtil.timer();
		HttpResponse body = HttpRequest.get(url).charset("GBK").execute();
		long interval = timer.interval();
		timer.restart();
		Console.log(body.body());
		long interval2 = timer.interval();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Ignore
	public void customGetTest() {
		// 自定义构建HTTP GET请求，发送Http GET请求，针对HTTPS安全加密，可以自定义SSL
		HttpRequest request = HttpRequest.get(url)
				// 自定义返回编码
				.charset(CharsetUtil.CHARSET_GBK)
				// 禁用缓存
				.disableCache()
				// 自定义SSL版本
				.setSSLProtocol(SSLSocketFactoryBuilder.TLSv12);
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void getDeflateTest() {
		String res = HttpRequest.get("https://comment.bilibili.com/67573272.xml")
				.execute().body();
		Console.log(res);
	}

	@Test
	@Ignore
	public void bodyTest(){
		String ddddd1 = HttpRequest.get("https://baijiahao.baidu.com/s").body("id=1625528941695652600").execute().body();
		Console.log(ddddd1);
	}
}
