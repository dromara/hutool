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

import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.net.url.UrlQueryUtil;
import org.dromara.hutool.core.util.CharsetUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Issue3536Test {

	@Test
	public void urlWithFormUrlEncodedTest() {
		final String url = "https://hutool.cn/test";
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("redirect_uri", "https://api.hutool.cn/v1/test");
		paramMap.put("scope", "a,b,c你");

		final String s = HttpUtil.urlWithFormUrlEncoded(url, paramMap, CharsetUtil.UTF_8);
		assertEquals("https://hutool.cn/test?scope=a,b,c%E4%BD%A0&redirect_uri=https://api.hutool.cn/v1/test", s);
	}

	@Test
	public void toQueryTest() {
		final Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("redirect_uri", "https://api.hutool.cn/v1/test");
		paramMap.put("scope", "a,b,c你");

		final String params = UrlQueryUtil.toQuery(paramMap, CharsetUtil.UTF_8, UrlQuery.EncodeMode.STRICT);
		assertEquals("scope=a%2Cb%2Cc%E4%BD%A0&redirect_uri=https%3A%2F%2Fapi.hutool.cn%2Fv1%2Ftest", params);
	}
}
