package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class JdkEngineTest {

	@Test
	@Disabled
	public void getTest(){
		final ClientEngine engine = new JdkClientEngine();

		final Request req = Request.of("https://www.hutool.cn/").method(Method.GET);
		final Response res = engine.send(req);

		Console.log(res.getStatus());
		Console.log(res.bodyStr());
	}
}
