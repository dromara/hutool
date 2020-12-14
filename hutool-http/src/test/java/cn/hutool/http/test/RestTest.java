package cn.hutool.http.test;

import cn.hutool.core.lang.Console;
import cn.hutool.http.Header;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
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
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Assert.assertEquals("application/json;charset=UTF-8", request.header("Content-Type"));
	}

	@Test
	@Ignore
	public void postTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")//
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}

	@Test
	@Ignore
	public void postTest2() {
		String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.createObj()//
				.set("aaa", "aaaValue")
				.set("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Ignore
	public void getWithBodyTest() {
		HttpRequest request = HttpRequest.get("http://localhost:8888/restTest")//
				.header(Header.CONTENT_TYPE, "application/json")
				.body(JSONUtil.createObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.execute().body());
	}
}
