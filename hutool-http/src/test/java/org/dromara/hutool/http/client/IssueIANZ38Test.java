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
import org.dromara.hutool.http.client.engine.ClientEngineFactory;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class IssueIANZ38Test {

	@Test
	@Disabled
	void toStringTest() {
		final Request request = Request.of("https://hutool.cn/");
		try(final Response response = ClientEngineFactory.createEngine("JdkClient").send(request)) {
			final String bodyStr = response.bodyStr();

			Console.log(bodyStr);
			// 读取过一次bodyStr后，再读取就关闭流了，此处做修改，读取body时同步响应body到内存
			Console.log(response.toString());
		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}
}
