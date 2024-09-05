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

import org.dromara.hutool.core.date.DateUtil;
import org.dromara.hutool.core.net.url.UrlBuilder;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.http.client.Request;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

public class IssueIA80XITest {

	@Test
	void urlWithFormTest() {
		final Map<String, Object> param = new LinkedHashMap<>();
		param.put("date", DateUtil.parse("2024-06-25 14:16:47"));

		// 未经过自动编码的
		final Request request = Request.ofWithoutEncode("http://api.hutool.cn/login?type=aaa").form(param);
		final UrlBuilder urlBuilder = request.handledUrl();
		Assertions.assertEquals("http://api.hutool.cn/login?type=aaa&date=2024-06-25 14:16:47", urlBuilder.toString());

		// 经过自动编码的
		final Request request2 = Request.of("http://api.hutool.cn/login?type=aaa", CharsetUtil.UTF_8).form(param);
		final UrlBuilder urlBuilder2 = request2.handledUrl();
		Assertions.assertEquals("http://api.hutool.cn/login?type=aaa&date=2024-06-25%2014:16:47", urlBuilder2.toString());

		// 经过自动编码的
		final Request request3 = Request.of("http://api.hutool.cn/login?type=aaa").form(param);
		final UrlBuilder urlBuilder3 = request3.handledUrl();
		Assertions.assertEquals("http://api.hutool.cn/login?type=aaa&date=2024-06-25%2014:16:47", urlBuilder3.toString());
	}
}
