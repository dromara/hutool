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

package org.dromara.hutool.http.client;

import org.dromara.hutool.core.lang.Console;
import org.dromara.hutool.http.HttpUtil;
import org.dromara.hutool.http.client.engine.ClientEngine;
import org.dromara.hutool.http.meta.Method;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue3240Test {
	@Test
	@Disabled
	void okHttpTest() {
		String url = "https://gh.yubue.cn/https://github.com/espressif/arduino-esp32/releases/download/2.0.11/package_esp32_dev_index.json";
		final ClientEngine engine = HttpUtil.createClient("okhttp");
		final Response send = engine.send(Request.of(url).method(Method.GET));
		Console.log(send.body().getString());
	}

	@Test
	@Disabled
	void httpClient4Test() {
		String url = "https://gh.yubue.cn/https://github.com/espressif/arduino-esp32/releases/download/2.0.11/package_esp32_dev_index.json";
		final ClientEngine engine = HttpUtil.createClient("okhttp");
		final Response send = engine.send(Request.of(url).method(Method.GET));
		Console.log(send.body().getString());
	}
}
