package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.engine.jdk.HttpRequest;
import cn.hutool.http.meta.Header;
import cn.hutool.json.JSONUtil;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Rest类型请求单元测试
 *
 * @author looly
 *
 */
public class RestTest {

	@Test
	public void contentTypeTest() {
		final HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Assert.assertEquals("application/json;charset=UTF-8", request.header(Header.CONTENT_TYPE));
	}

	@Test
	@Ignore
	public void postTest() {
		final HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void postTest2() {
		final String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.ofObj()//
				.set("aaa", "aaaValue")
				.set("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Ignore
	public void getWithBodyTest() {
		final HttpRequest request = HttpRequest.get("http://localhost:8888/restTest")//
				.header(Header.CONTENT_TYPE, "application/json")
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void getWithBodyTest2() {
		final HttpRequest request = HttpRequest.get("https://ad.oceanengine.com/open_api/2/advertiser/info/")//
				// Charles代理
				.setHttpProxy("localhost", 8888)
				.header("Access-Token","")
				.body(JSONUtil.ofObj()
						.set("advertiser_ids", new Long[] {1690657248243790L})
						.set("fields", new String[] {"id", "name", "status"}).toString());
		Console.log(request);
		Console.log(request.execute().body());
	}
}
