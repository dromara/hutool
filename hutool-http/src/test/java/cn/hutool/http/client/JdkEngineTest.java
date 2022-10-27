package cn.hutool.http.client;

import cn.hutool.core.lang.Console;
import cn.hutool.http.client.engine.jdk.JdkClientEngine;
import cn.hutool.http.meta.Method;
import org.junit.Ignore;
import org.junit.Test;

public class JdkEngineTest {

	@Test
	@Ignore
	public void getTest(){
		final ClientEngine engine = new JdkClientEngine();

		final Request req = Request.of("https://www.hutool.cn/").method(Method.GET);
		final Response res = engine.send(req);

		Console.log(res.getStatus());
		Console.log(res.bodyStr());
	}
}
