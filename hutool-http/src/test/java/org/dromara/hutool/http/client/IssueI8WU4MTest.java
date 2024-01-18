package org.dromara.hutool.http.client;

import org.dromara.hutool.http.HttpGlobalConfig;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.engine.okhttp.OkHttpEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI8WU4MTest {

	@Test
	@Disabled
	void timeoutTest() {
		//设置超时，单位毫秒，这里1毫秒，按道理100%超时
		HttpGlobalConfig.setTimeout(1);

		final String body = HttpUtil.createGet("https://restapi.amap.com/v3/ip?Key=ad054b1810672fb0ff6107cd71518837")
			.header(HeaderName.CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8")
			.header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36 Edg/120.0.0.0")
			.send(new OkHttpEngine())
			.body().
			getString();
		System.out.println(body);
	}
}
