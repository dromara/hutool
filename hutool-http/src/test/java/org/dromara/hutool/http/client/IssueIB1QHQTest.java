package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIB1QHQTest {
	@Test
	@Disabled
	void requestByOkHttpTest() {
		for (int i = 0; i < 3; i++) {
			String response = ClientEngineFactory.createEngine("OkHttp")
				.send(Request
					.of("https://hutool.cn")
					.method(Method.POST)
					.body("123")
				).bodyStr();
			Console.log("response: {}", response);
		}
	}
}
