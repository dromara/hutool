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
import org.dromara.hutool.http.client.engine.jdk.JdkClientEngine;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class Issue444Test {
	@Test
	@Disabled
	void getTest() {
		final String s = Request.of("http://localhost:8888").send(new JdkClientEngine()).bodyStr();
		Console.log(s);
	}
}