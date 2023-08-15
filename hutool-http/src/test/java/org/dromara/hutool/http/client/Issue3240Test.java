/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
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
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.client.engine.httpclient4.HttpClient4Engine;
import org.dromara.hutool.http.client.engine.okhttp.OkHttpEngine;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3240Test {
	@Test
	@Disabled
	void okHttpTest() {
		String url = "https://gh.yubue.cn/https://github.com/espressif/arduino-esp32/releases/download/2.0.11/package_esp32_dev_index.json";
		final ClientEngine engine = new OkHttpEngine();
		final Response send = engine.send(Request.of(url).method(Method.GET));
		Console.log(send.body().getString());
	}

	@Test
	@Disabled
	void httpClient4Test() {
		String url = "https://gh.yubue.cn/https://github.com/espressif/arduino-esp32/releases/download/2.0.11/package_esp32_dev_index.json";
		final ClientEngine engine = new HttpClient4Engine();
		final Response send = engine.send(Request.of(url).method(Method.GET));
		Console.log(send.body().getString());
	}
}
