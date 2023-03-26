package cn.hutool.http;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.io.resource.FileResource;
import cn.hutool.core.io.resource.HttpResource;
import cn.hutool.core.lang.Console;
import cn.hutool.http.client.Request;
import cn.hutool.http.client.Response;
import cn.hutool.http.client.body.ResourceBody;
import cn.hutool.http.meta.ContentType;
import cn.hutool.http.meta.Method;
import org.junit.Ignore;
import org.junit.Test;

public class Issue2901Test {

	@Test
	@Ignore
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
