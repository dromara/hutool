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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * FastJSON和Hutool不同策略测试<br>
 * 在put时，Hutool采用“即时转换”的方式，不再与原对象有关联，<br>
 * 而FastJSON采用“暂存”方式，put进JSONObject的对象不变，只有序列化时转换
 */
public class IssueI7VM64Test {

	@Test
	void hutoolJSONTest() {
		final HashMap<String, Object> map = new HashMap<>();
		map.put("a", "1");

		final JSONObject jsonObject = new JSONObject();
		jsonObject.put("c", map);
		map.put("b", 2);

		//Console.log("Hutool JSON: " + jsonObject);
		Assertions.assertEquals("{\"c\":{\"a\":\"1\"}}", jsonObject.toString());
	}

	@Test
	void fastJSONTest() {
		final HashMap<String, Object> map = new HashMap<>();
		map.put("a", "1");

		final com.alibaba.fastjson.JSONObject jsonObject = new com.alibaba.fastjson.JSONObject();
		jsonObject.put("c", map);

		map.put("b", 2);

		//Console.log("FastJSON: " + jsonObject);
		Assertions.assertEquals("{\"c\":{\"a\":\"1\",\"b\":2}}", jsonObject.toString());
	}

	@Test
	void fastJSON2Test() {
		final HashMap<String, Object> map = new HashMap<>();
		map.put("a", "1");

		final com.alibaba.fastjson2.JSONObject jsonObject = new com.alibaba.fastjson2.JSONObject();
		jsonObject.put("c", map);

		map.put("b", 2);

		//Console.log("FastJSON2 " + jsonObject);
		Assertions.assertEquals("{\"c\":{\"a\":\"1\",\"b\":2}}", jsonObject.toString());
	}
}
