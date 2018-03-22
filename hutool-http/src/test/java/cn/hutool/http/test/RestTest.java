package cn.hutool.http.test;

import org.junit.Ignore;
import org.junit.Test;

import cn.hutool.core.lang.Console;
import cn.hutool.http.HttpRequest;
import cn.hutool.json.JSONUtil;

public class RestTest {
	
	@Test
	@Ignore
	public void postTest() {
		HttpRequest request = HttpRequest.post("http://localhost:8090/rest/restTest/")
				.body(JSONUtil.createObj().put("aaa", "aaaValue").put("键2", "值2"));
		Console.log(request.execute().body());
	}
	
	@Test
	@Ignore
	public void postTest2() {
		HttpRequest request = HttpRequest.post("http://211.162.39.204:8181/jeesite-simple/a/open/bizGwbnService/test")
				.body(JSONUtil.createObj().put("aaa", "aaaValue").put("键2", "值2"));
		Console.log(request.execute().body());
	}
	
}
