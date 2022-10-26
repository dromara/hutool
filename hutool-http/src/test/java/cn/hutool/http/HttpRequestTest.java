package cn.hutool.http;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.StopWatch;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Console;
import cn.hutool.core.net.ssl.SSLProtocols;
import cn.hutool.core.net.url.UrlBuilder;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.client.engine.jdk.HttpRequest;
import cn.hutool.http.client.engine.jdk.HttpResponse;
import cn.hutool.http.meta.Header;
import cn.hutool.http.meta.Method;
import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link HttpRequest}单元测试
 *
 * @author Looly
 */
@SuppressWarnings("resource")
public class HttpRequestTest {
	final String url = "http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2";

	@Test
	@Ignore
	public void getHttpsTest() {
		final String body = HttpRequest.get("https://www.hutool.cn/").timeout(10).execute().body();
		Console.log(body);
	}

	@Test
	@Ignore
	public void getHttpsThenTest() {
		HttpRequest
				.get("https://hutool.cn")
				.then(response -> Console.log(response.body()));
	}

	@Test
	@Ignore
	public void getCookiesTest() {
		// 检查在Connection关闭情况下Cookie是否可以正常获取
		final HttpResponse res = HttpRequest.get("https://www.oschina.net/").execute();
		final String body = res.body();
		Console.log(res.getCookies());
		Console.log(body);
	}

	@Test
	@Ignore
	public void toStringTest() {
		final String url = "http://gc.ditu.aliyun.com/geocoding?ccc=你好";

		final HttpRequest request = HttpRequest.get(url).body("a=乌海");
		Console.log(request.toString());
	}

	@Test
	@Ignore
	public void asyncHeadTest() {
		final HttpResponse response = HttpRequest.head(url).execute();
		final Map<String, List<String>> headers = response.headers();
		Console.log(headers);
		Console.log(response.body());
	}

	@Test
	@Ignore
	public void asyncGetTest() {
		final StopWatch timer = DateUtil.createStopWatch();
		timer.start();
		final HttpResponse body = HttpRequest.get(url).charset("GBK").executeAsync();
		timer.stop();
		final long interval = timer.getLastTaskTimeMillis();
		timer.start();
		Console.log(body.body());
		timer.stop();
		final long interval2 = timer.getLastTaskTimeMillis();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Ignore
	public void syncGetTest() {
		final StopWatch timer = DateUtil.createStopWatch();
		timer.start();
		final HttpResponse body = HttpRequest.get(url).charset("GBK").execute();
		timer.stop();
		final long interval = timer.getLastTaskTimeMillis();

		timer.start();
		Console.log(body.body());
		timer.stop();
		final long interval2 = timer.getLastTaskTimeMillis();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Ignore
	public void customGetTest() {
		// 自定义构建HTTP GET请求，发送Http GET请求，针对HTTPS安全加密，可以自定义SSL
		final HttpRequest request = HttpRequest.get(url)
				// 自定义返回编码
				.charset(CharsetUtil.GBK)
				// 禁用缓存
				.disableCache()
				// 自定义SSL版本
				.setSSLProtocol(SSLProtocols.TLSv12);
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void getDeflateTest() {
		final HttpResponse res = HttpRequest.get("https://comment.bilibili.com/67573272.xml")
				.header(Header.ACCEPT_ENCODING, "deflate")
				.execute();
		Console.log(res.header(Header.CONTENT_ENCODING));
		Console.log(res.body());
	}

	@Test
	@Ignore
	public void bodyTest() {
		final String ddddd1 = HttpRequest.get("https://baijiahao.baidu.com/s").body("id=1625528941695652600").execute().body();
		Console.log(ddddd1);
	}

	/**
	 * 测试GET请求附带body体是否会变更为POST
	 */
	@Test
	@Ignore
	public void getLocalTest() {
		final List<String> list = new ArrayList<>();
		list.add("hhhhh");
		list.add("sssss");

		final Map<String, Object> map = new HashMap<>(16);
		map.put("recordId", "12321321");
		map.put("page", "1");
		map.put("size", "2");
		map.put("sizes", list);

		HttpRequest
				.get("http://localhost:8888/get")
				.form(map)
				.then(resp -> Console.log(resp.body()));
	}

	@Test
	@Ignore
	public void getWithoutEncodeTest() {
		final String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		final HttpRequest get = HttpUtil.createGet(url);
		Console.log(get.getUrl());
		final HttpResponse execute = get.execute();
		Console.log(execute.body());
	}

	@Test
	@Ignore
	public void followRedirectsTest() {
		// 从5.7.19开始关闭JDK的自动重定向功能，改为手动重定向
		// 当有多层重定向时，JDK的重定向会失效，或者说只有最后一个重定向有效，因此改为手动更易控制次数
		// 此链接有两次重定向，当设置次数为1时，表示最多执行一次重定向，即请求2次
		final String url = "http://api.rosysun.cn/sjtx/?type=2";
//		String url = "https://api.btstu.cn/sjtx/api.php?lx=b1";

		// 方式1：全局设置
		HttpGlobalConfig.setMaxRedirectCount(1);
		HttpResponse execute = HttpRequest.get(url).execute();
		Console.log(execute.getStatus(), execute.header(Header.LOCATION));

		// 方式2，单独设置
		execute = HttpRequest.get(url).setMaxRedirectCount(1).execute();
		Console.log(execute.getStatus(), execute.header(Header.LOCATION));
	}

	@Test
	@Ignore
	public void getWithFormTest(){
		final String url = "https://postman-echo.com/get";
		final Map<String, Object> map = new HashMap<>();
		map.put("aaa", "application+1@qqq.com");
		final HttpRequest request =HttpUtil.createGet(url).form(map);
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void urlWithParamIfGetTest(){
		final UrlBuilder urlBuilder = new UrlBuilder();
		urlBuilder.setScheme("https").setHost("hutool.cn");

		final HttpRequest httpRequest = new HttpRequest(urlBuilder);
		httpRequest.method(Method.GET).execute();
	}

	@Test
	@Ignore
	public void getCookieTest(){
		final HttpResponse execute = HttpRequest.get("http://localhost:8888/getCookier").execute();
		Console.log(execute.getCookies());
	}

	@Test
	public void optionsTest() {
		final HttpRequest options = HttpRequest.options("https://hutool.cn");
		Assert.notNull(options.toString());
	}

	@Test
	public void deleteTest() {
		final HttpRequest options = HttpRequest.delete("https://hutool.cn");
		Assert.notNull(options.toString());
	}

	@Test
	public void traceTest() {
		final HttpRequest options = HttpRequest.trace("https://hutool.cn");
		Assert.notNull(options.toString());
	}

	@Test
	public void getToStringTest() {
		final HttpRequest a = HttpRequest.get("https://hutool.cn/").form("a", 1);
		Assert.notNull(a.toString());
	}

	@Test
	public void postToStringTest() {
		final HttpRequest a = HttpRequest.post("https://hutool.cn/").form("a", 1);
		Console.log(a.toString());
	}
}
