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
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class IssueI5TPSYTest {

	@Test
	@Disabled
	public void redirectTest() {
		final String url = "https://bsxt.gdzwfw.gov.cn/UnifiedReporting/auth/newIndex";
		final Response res = HttpUtil.send(Request.of(url)
						.setMaxRedirects(2)
				.header(HeaderName.USER_AGENT, "PostmanRuntime/7.29.2")
				.cookie("jsessionid=s%3ANq6YTcIHQWrHkEqOSxiQNijDMhoFNV4_.h2MVD1CkW7sOZ60OSnPs7m4K%2FhENfYy%2FdzjKvSiZF4E"));
		Console.log(res.body());
	}
}
