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

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.dromara.hutool.http.client.engine.okhttp.OkHttpEngine;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Test;

public class HttpClientSendTest {
	@Test
	void sendTest() {
		final String json = JSONUtil.ofObj()
			.set("contract_id", "1387572813382368")
			.set("txt_url", "http://47.99.192.138:9000/oss/20230915/test.txt")
			.set("docx_url", "http://47.99.192.138:9000/oss/20230915/test.docx")
			.toString();

		final Response response =
			Request.of("http://47.99.192.138:8012/extract/counterparty/")
				.method(Method.POST)
				.auth(
					"eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJsb2dpbklkIjoidGVzdGxvZ2luMSIsImV4cCI6MTU1NjcxMjAzNn0.1ukpIm0xAqXFAvHk7U8eFJbhpiYTV8VtdaiBl_JqQ0s")
				.body(json)
				.send(new OkHttpEngine());

		Console.log(response);
	}
}
