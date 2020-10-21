package cn.hutool.http.test;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.TimeInterval;
import cn.hutool.core.lang.Console;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.ssl.SSLSocketFactoryBuilder;
import cn.hutool.json.JSONUtil;
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
	public void bodyTest() {
		String ddddd1 = HttpRequest.get("https://baijiahao.baidu.com/s").body("id=1625528941695652600").execute().body();
		Console.log(ddddd1);
	}

	/**
	 * 测试GET请求附带body体是否会变更为POST
	 */
	@Test
	@Ignore
	public void getLocalTest() {
		List<String> list = new ArrayList<>();
		list.add("hhhhh");
		list.add("sssss");

		Map<String, Object> map = new HashMap<>(16);
		map.put("recordId", "12321321");
		map.put("page", "1");
		map.put("size", "2");
		map.put("sizes", list);

		String s = JSONUtil.toJsonStr(map);
		HttpRequest request = HttpUtil.createGet("http://localhost:8888/get");
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void getWithoutEncodeTest(){
		String url = "https://img-cloud.voc.com.cn/140/2020/09/03/c3d41b93e0d32138574af8e8b50928b376ca5ba61599127028157.png?imageMogr2/auto-orient/thumbnail/500&pid=259848";
		HttpRequest get = HttpUtil.createGet(url);
		Console.log(get.getUrl());
		HttpResponse execute = get.execute();
		Console.log(execute.body());
	}

}
