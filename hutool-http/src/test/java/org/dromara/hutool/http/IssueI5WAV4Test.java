/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          https://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.http;

import org.dromara.hutool.http.client.Request;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class IssueI5WAV4Test {

	@Test
	@Disabled
	public void getTest(){
		//测试代码
		final Map<String, Object> map = new HashMap<>();
		map.put("taskID", 370);
		map.put("flightID", 2879);


		@SuppressWarnings("resource")
		final String body = Request.of("http://localhost:8884/api/test/testHttpUtilGetWithBody").body(JSONUtil.toJsonStr(map)).send().bodyStr();
		System.out.println("使用hutool返回结果:" + body);
	}
}
