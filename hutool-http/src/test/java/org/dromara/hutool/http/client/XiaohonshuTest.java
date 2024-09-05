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
import org.dromara.hutool.http.client.engine.okhttp.OkHttpEngine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class XiaohonshuTest {
	@Test
	@Disabled
	void postTest() {
		final long l = System.currentTimeMillis();
		final String sign = "oauth.getAccessToken?appId=880c43b57a97425d9a06&timestamp=" + l
			+ "&version=2.0d9758815286df7a3075d64785df2b890";

		final HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("sign", sign);
		paramMap.put("appId", "880c43b57a97425d9a06");  //880c43b57a97425d9a06
		paramMap.put("timestamp", l);
		paramMap.put("version", "2.0");
		paramMap.put("method", "oauth.getAccessToken");
		paramMap.put("code", "code-266ef7f29fff4f0d80d4bd23f579391e-2097e077fad84eb0a5ec4a44a8de1116");
		final String json = JSONUtil.toJsonStr(paramMap);

		final Request request = Request.of("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller")
			.header(HeaderName.CONTENT_TYPE, "application/json;charset=UTF-8")
			.method(Method.POST)
			.header(HeaderName.ACCEPT_ENCODING, "gzip")
			.body(json);

		final Response res = request.send(new OkHttpEngine());
		Console.log(res.header(HeaderName.CONTENT_ENCODING));
		Console.log(res.bodyStr());
	}
}
