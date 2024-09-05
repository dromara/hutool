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

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.net.url.UrlQuery;
import org.dromara.hutool.core.util.CharsetUtil;
import org.dromara.hutool.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueIAFKWPTest {

	@Test
	void urlWithFormTest() {
		final JSONObject obj = new JSONObject();
		obj.put("fields", ListUtil.of("1", "2", "good"));

		final Map<String, Object> params = new HashMap<>();
		params.put("query", obj.toString());

		// form-url-encoded模式下所有字符转义
		String build = UrlQuery.of(params, UrlQuery.EncodeMode.FORM_URL_ENCODED).build(CharsetUtil.UTF_8);
		String s = HttpUtil.urlWithForm("https://hutool.cn", build, CharsetUtil.UTF_8, false);
		Assertions.assertEquals("https://hutool.cn?query=%7B%22fields%22%3A%5B%221%22%2C%222%22%2C%22good%22%5D%7D", s);

		// 标准模式下只转义特定字符
		build = UrlQuery.of(params, UrlQuery.EncodeMode.NORMAL).build(CharsetUtil.UTF_8);
		s = HttpUtil.urlWithForm("https://hutool.cn", build, CharsetUtil.UTF_8, false);
		Assertions.assertEquals("https://hutool.cn?query=%7B%22fields%22:%5B%221%22,%222%22,%22good%22%5D%7D", s);
	}
}
