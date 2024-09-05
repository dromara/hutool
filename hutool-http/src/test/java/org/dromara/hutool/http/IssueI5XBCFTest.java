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
import org.dromara.hutool.http.client.Response;
import org.dromara.hutool.http.meta.HeaderName;
import org.brotli.dec.BrotliInputStream;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI5XBCFTest {

	@Test
	@Disabled
	public void getTest() {
		GlobalCompressStreamRegister.INSTANCE.register("br", BrotliInputStream.class);

		@SuppressWarnings("resource")
		final Response s = Request.of("https://static-exp1.licdn.com/sc/h/br/1cp0oqz322bdprj3qd4pojqix")
				.header(HeaderName.ACCEPT_ENCODING, "br")
				.send();
		Console.log(s.body());
	}
}
