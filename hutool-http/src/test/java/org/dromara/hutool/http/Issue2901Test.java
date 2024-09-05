/*
 * Copyright (c) 2024 Hutool Team and hutool.cn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.dromara.hutool.http;

import org.dromara.hutool.core.io.IoUtil;
import org.dromara.hutool.core.io.resource.FileResource;
import org.dromara.hutool.core.io.resource.HttpResource;
import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.body.ResourceBody;
import org.dromara.hutool.http.meta.ContentType;
import org.dromara.hutool.http.meta.Method;
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
