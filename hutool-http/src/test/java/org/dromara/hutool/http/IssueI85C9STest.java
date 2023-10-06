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
import org.dromara.hutool.http.client.engine.httpclient4.HttpClient4Engine;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class IssueI85C9STest {


	@Test
	@Disabled
	void getWithFormTest() {
		final Response send = Request.of("http://localhost:8888/formTest")
			.method(Method.GET)
			.form(MapBuilder.of(new HashMap<String, Object>()).put("a", 1).put("b", 2).build())
			.send(new HttpClient4Engine());

		Console.log(send.bodyStr());
	}
}
