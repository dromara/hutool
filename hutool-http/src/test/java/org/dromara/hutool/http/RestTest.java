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
import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Rest类型请求单元测试
 *
 * @author looly
 *
 */
public class RestTest {

	@Test
	public void contentTypeTest() {
		final Request request = Request.of("http://localhost:8090/rest/restTest/")
				.method(Method.POST)
				.body(JSONUtil.ofObj()
						.putObj("aaa", "aaaValue")
						.putObj("键2", "值2").toString());
		Assertions.assertEquals("application/json;charset=UTF-8", request.header(HeaderName.CONTENT_TYPE));
	}

	@SuppressWarnings("resource")
	@Test
	@Disabled
	public void postTest() {
		final Request request = Request.of("http://localhost:8090/rest/restTest/")
				.method(Method.POST)
				.body(JSONUtil.ofObj()
						.putObj("aaa", "aaaValue")
						.putObj("键2", "值2").toString());
		Console.log(request.send().body());
	}

	@Test
	@Disabled
	public void postTest2() {
		final String result = HttpUtil.post("http://localhost:8090/rest/restTest/", JSONUtil.ofObj()//
				.putObj("aaa", "aaaValue")
				.putObj("键2", "值2").toString());
		Console.log(result);
	}

	@Test
	@Disabled
	public void getWithBodyTest() {
		final Request request = Request.of("http://localhost:8888/restTest")//
				.header(HeaderName.CONTENT_TYPE, "application/json")
				.body(JSONUtil.ofObj()
						.putObj("aaa", "aaaValue")
						.putObj("键2", "值2").toString());
		//noinspection resource
		Console.log(request.send().body());
	}
}
