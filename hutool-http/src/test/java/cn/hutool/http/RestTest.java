package cn.hutool.http;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.Request;
import cn.hutool.http.meta.Header;
import cn.hutool.http.meta.Method;
import cn.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Rest类型请求单元测试
 *
 * @author looly
 *
 */
public class RestTest {

	@Test
	public void contentTypeTest() {
		final Request request = Request.of("http://localhost:8090/rest/restTest/")
				.method(Method.POST)
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Assertions.assertEquals("application/json;charset=UTF-8", request.header(Header.CONTENT_TYPE));
	}

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void postTest() {
		final Request request = Request.of("http://localhost:8090/rest/restTest/")
				.method(Method.POST)
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		Console.log(request.send().body());
	}

	@Test
	@Disabled
	public void postTest2() {
		final String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.ofObj()//
				.set("aaa", "aaaValue")
				.set("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Disabled
	public void getWithBodyTest() {
		final Request request = Request.of("http://localhost:8888/restTest")//
				.header(Header.CONTENT_TYPE, "application/json")
				.body(JSONUtil.ofObj()
						.set("aaa", "aaaValue")
						.set("键2", "值2").toString());
		//noinspection resource
		Console.log(request.send().body());
	}
}
