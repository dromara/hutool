package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.HttpGlobalConfig;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueIBC5N8Test {
	@Test
	@Disabled
	public void getBadSSLTest(){
		HttpGlobalConfig.setTrustAnyHost(true);
		requestBadSSL("httpclient4");
		requestBadSSL("httpclient5");
		requestBadSSL("okhttp");
		requestBadSSL("jdkClient");
	}

	private void requestBadSSL(final String engineName) {
		final ClientEngine engine = HttpUtil.createClient(engineName);

		final Request req = Request.of("https://expired.badssl.com/");
		final Response res = engine.send(req);
		Console.log(res.getStatus());
	}
}
