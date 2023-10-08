/*
 * Copyright (c) 2023. looly(loolly@aliyun.com)
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
import org.dromara.hutool.http.client.engine.httpclient5.HttpClient5Engine;
import org.dromara.hutool.http.meta.HeaderName;
import org.dromara.hutool.http.meta.Method;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.HashMap;

public class XiaohonshuTest {
	@Test
	@Disabled
	void postTest() {
		long l = (new Date().getTime());
		String sign = "oauth.getAccessToken?appId=880c43b57a97425d9a06&timestamp="+l
			+"&version=2.0d9758815286df7a3075d64785df2b890";

		HashMap<String, Object> paramMap = new HashMap<>();
		paramMap.put("sign",sign);
		paramMap.put("appId", "880c43b57a97425d9a06");  //880c43b57a97425d9a06
		paramMap.put("timestamp", l);
		paramMap.put("version", "2.0");
		paramMap.put("method", "oauth.getAccessToken");
		paramMap.put("code", "code-266ef7f29fff4f0d80d4bd23f579391e-2097e077fad84eb0a5ec4a44a8de1116");
		String json = JSONUtil.toJsonStr(paramMap);

		Request request = Request.of("https://ark.xiaohongshu.com/ark/open_api/v3/common_controller")
            .header(HeaderName.CONTENT_TYPE, "application/json;charset=UTF-8")
			.method(Method.POST)
			.body(json);
		Console.log(request.send(new HttpClient5Engine()).bodyStr());
	}
}
