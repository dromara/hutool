package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.dromara.hutool.http.ssl.SSLInfo;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI7ZRJUTest {

	@SuppressWarnings({"resource", "TestFailedLine"})
	@Test
	@Disabled
	void getBadSSlTest() {
		final Response response = Request.of("https://expired.badssl.com/")
			.send(new JdkClientEngine().init(ClientConfig.of().setSSLInfo(SSLInfo.DEFAULT)));

		Console.log(response.body());
	}
}
