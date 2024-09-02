package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IssueIANZ38Test {

	@Test
	@Disabled
	void toStringTest() {
		final Request request = Request.of("https://hutool.cn/");
		try(final Response response = ClientEngineFactory.createEngine("JdkClient").send(request)) {
			final String bodyStr = response.bodyStr();

			Console.log(bodyStr);
			// 读取过一次bodyStr后，再读取就关闭流了，此处做修改，读取body时同步响应body到内存
			Console.log(response.toString());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
