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
		jsonObject.set("c", map);
		map.put("b", 2);

		//Console.log("Hutool JSON: " + jsonObject);
		Assertions.assertEquals("{\"c\":{\"a\":\"1\"}}", jsonObject.toString());
	}
}
