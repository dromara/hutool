/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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

public class IssueI85C9STest {

	private final ClientEngine  engine = new HttpClient5Engine();

	@Test
	@Disabled
	void getWithFormTest() {

		final Response send = Request.of("http://localhost:8888/formTest")
			.method(Method.GET)
			.form(MapBuilder.of("a", (Object)1).put("b", 2).build())
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
			.form(MapBuilder.of("a", (Object)1).put("b", 2).build())
			.send(engine);

		Assertions.assertEquals("{a=[1], b=[2], c=[3]}", send.bodyStr());
	}
}
