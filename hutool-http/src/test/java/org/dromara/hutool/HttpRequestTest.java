package org.dromara.hutool;

import org.dromara.hutool.client.Request;
import org.dromara.hutool.client.Response;
import org.dromara.hutool.date.DateUtil;
import org.dromara.hutool.date.StopWatch;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.map.MapUtil;
import org.dromara.hutool.meta.Header;
import org.dromara.hutool.meta.Method;
import org.dromara.hutool.net.url.UrlBuilder;
import org.dromara.hutool.util.CharsetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * {@link Request}单元测试
 *
 * @author Looly
 */
@SuppressWarnings("resource")
public class HttpRequestTest {
	final String url = "http://photo.qzone.qq.com/fcgi-bin/fcg_list_album?uin=88888&outstyle=2";

	@Test
	@Disabled
	public void getHttpsThenTest() {
		Request.of("https://hutool.cn")
				.send()
				.then(response -> Console.log(response.body()));
	}

	@Test
	@Disabled
	public void getCookiesTest() {
		// 检查在Connection关闭情况下Cookie是否可以正常获取
		final Response res = Request.of("https://www.oschina.net/").send();
		final String body = res.bodyStr();
		Console.log(res.getCookieStr());
		Console.log(body);
	}

	@Test
	@Disabled
	public void toStringTest() {
		final String url = "http://gc.ditu.aliyun.com/geocoding?ccc=你好";

		final Request request = Request.of(url).body("a=乌海");
		Console.log(request.toString());
	}

	@Test
	@Disabled
	public void asyncHeadTest() {
		final Response response = Request.of(url).method(Method.HEAD).send();
		final Map<String, List<String>> headers = response.headers();
		Console.log(headers);
		Console.log(response.body());
	}

	@Test
	@Disabled
	public void asyncGetTest() {
		final StopWatch timer = DateUtil.createStopWatch();
		timer.start();
		final Response body = Request.of(url).charset(CharsetUtil.GBK).send();
		timer.stop();
		final long interval = timer.getLastTaskTimeMillis();
		timer.start();
		Console.log(body.body());
		timer.stop();
		final long interval2 = timer.getLastTaskTimeMillis();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Disabled
	public void syncGetTest() {
		final StopWatch timer = DateUtil.createStopWatch();
		timer.start();
		final Response body = Request.of(url).charset(CharsetUtil.GBK).send();
		timer.stop();
		final long interval = timer.getLastTaskTimeMillis();

		timer.start();
		Console.log(body.body());
		timer.stop();
		final long interval2 = timer.getLastTaskTimeMillis();
		Console.log("Async response spend {}ms, body spend {}ms", interval, interval2);
	}

	@Test
	@Disabled
	public void getDeflateTest() {
		final Response res = Request.of("https://comment.bilibili.com/67573272.xml")
				.header(Header.ACCEPT_ENCODING, "deflate")
				.send();
		Console.log(res.header(Header.CONTENT_ENCODING));
		Console.log(res.body());
	}

	@Test
	@Disabled
	public void bodyTest() {
		final String ddddd1 = Request.of("https://baijiahao.baidu.com/s").body("id=1625528941695652600").send().bodyStr();
		Console.log(ddddd1);
	}

	/**
	 * 测试GET请求附带body体是否会变更为POST
	 */
	@Test
	@Disabled
	public void getLocalTest() {
		final List<String> list = new ArrayList<>();
		list.add("hhhhh");
		list.add("sssss");

		final Map<String, Object> map = new HashMap<>(16);
		map.put("recordId", "12321321");
		map.put("page", "1");
		map.put("size", "2");
		map.put("sizes", list);

		Request
				.of("http://localhost:8888/get")
				.form(map).send()
				.then(resp -> Console.log(resp.body()));
	}

	@Test
	@Disabled
	public void getWithoutEncodeTest() {
		final String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		final Request get = Request.of(url);
		Console.log(get.url());
		final Response execute = get.send();
		Console.log(execute.body());
	}

	@Test
	@Disabled
	public void followRedirectsTest() {
		// 从5.7.19开始关闭JDK的自动重定向功能，改为手动重定向
		// 当有多层重定向时，JDK的重定向会失效，或者说只有最后一个重定向有效，因此改为手动更易控制次数
		// 此链接有两次重定向，当设置次数为1时，表示最多执行一次重定向，即请求2次
		final String url = "http://api.rosysun.cn/sjtx/?type=2";
//		String url = "https://api.btstu.cn/sjtx/api.php?lx=b1";

		// 方式1：全局设置
		HttpGlobalConfig.setMaxRedirectCount(1);
		Response execute = Request.of(url).send();
		Console.log(execute.getStatus(), execute.header(Header.LOCATION));

		// 方式2，单独设置
		execute = Request.of(url).setMaxRedirectCount(1).send();
		Console.log(execute.getStatus(), execute.header(Header.LOCATION));
	}

	@Test
	@Disabled
	public void getWithFormTest(){
		final String url = "https://postman-echo.com/get";
		final Map<String, Object> map = new HashMap<>();
		map.put("aaa", "application+1@qqq.com");
		final Request request =Request.of(url).form(map);
		Console.log(request.send().body());
	}

	@Test
	@Disabled
	public void urlWithParamIfGetTest(){
		final UrlBuilder urlBuilder = new UrlBuilder();
		urlBuilder.setScheme("https").setHost("hutool.cn");

		final Request httpRequest = Request.of(urlBuilder);
		httpRequest.method(Method.GET).send();
	}

	@Test
	@Disabled
	public void getCookieTest(){
		final Response execute = Request.of("http://localhost:8888/getCookier").send();
		Console.log(execute.getCookieStr());
	}

	@Test
	public void optionsTest() {
		final Request options = Request.of("https://hutool.cn").method(Method.OPTIONS);
		Assertions.assertNotNull(options.toString());
	}

	@Test
	public void deleteTest() {
		final Request options = Request.of("https://hutool.cn").method(Method.DELETE);
		Assertions.assertNotNull(options.toString());
	}

	@Test
	public void traceTest() {
		final Request options = Request.of("https://hutool.cn").method(Method.TRACE);
		Assertions.assertNotNull(options.toString());
	}

	@Test
	public void getToStringTest() {
		final Request a = Request.of("https://hutool.cn/").form(MapUtil.of("a", 1));
		Assertions.assertNotNull(a.toString());
	}

	@Test
	public void postToStringTest() {
		final Request a = Request.of("https://hutool.cn/").method(Method.POST).form(MapUtil.of("a", 1));
		Console.log(a.toString());
	}

	@Test
	@Disabled
	public void issueI5Y68WTest() {
		final Response httpResponse = Request.of("http://82.157.17.173:8100/app/getAddress").send();
		Console.log(httpResponse.body());
	}
}
