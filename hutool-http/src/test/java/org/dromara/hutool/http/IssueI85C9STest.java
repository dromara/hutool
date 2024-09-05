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

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.core.map.MapBuilder;
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.client.engine.httpclient5.HttpClient5Engine;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * 配合SimpleServerTest测试
 */
public class IssueI85C9STest {

	private final ClientEngine engine = new HttpClient5Engine();

	@Test
	@Disabled
	void getWithFormTest() {

		final Response send = Request.of("http://localhost:8888/formTest")
			.method(Method.GET)
			.form(MapBuilder.of("a", (Object) 1).put("b", 2).build())
			.send(engine);

		final String bodyStr = send.bodyStr();
		Console.log(bodyStr);
		Assertions.assertEquals("{a=[1], b=[2]}", bodyStr);
	}

	@Test
	@Disabled
	void getWithFormAndUrlParamTest() {

		final Response send = Request.of("http://localhost:8888/formTest?c=3")
			.method(Method.GET)
			.form(MapBuilder.of("a", (Object) 1).put("b", 2).build())
			.send(engine);

		Assertions.assertEquals("{a=[1], b=[2], c=[3]}", send.bodyStr());
	}
}
