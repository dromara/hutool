package org.dromara.hutool;

import org.dromara.hutool.client.Request;
import org.dromara.hutool.client.Response;
import org.dromara.hutool.client.body.ResourceBody;
import org.dromara.hutool.io.IoUtil;
import org.dromara.hutool.io.resource.FileResource;
import org.dromara.hutool.io.resource.HttpResource;
import org.dromara.hutool.lang.Console;
import org.dromara.hutool.meta.ContentType;
import org.dromara.hutool.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue2901Test {

	@Test
	@Disabled
	public void bodyTest() {
		// 自定义请求体，请求体作为资源读取，解决一次性读取到内存的问题
		final Response res = Request.of("http://localhost:8888/restTest")
				.method(Method.POST)
				.body(new ResourceBody(
						new HttpResource(new FileResource("d:/test/test.jpg"), ContentType.OCTET_STREAM.getValue())))
				.send();

		Console.log(res.bodyStr());
		IoUtil.closeQuietly(res);
	}
}
