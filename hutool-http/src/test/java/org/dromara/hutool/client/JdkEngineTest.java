package org.dromara.hutool.client;

import org.dromara.hutool.lang.Console;
import org.dromara.hutool.client.engine.jdk.JdkClientEngine;
import org.dromara.hutool.meta.Method;
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
