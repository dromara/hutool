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

package org.dromara.hutool.json.issues;

import org.dromara.hutool.core.reflect.TypeReference;
import org.dromara.hutool.json.JSONObject;
import org.dromara.hutool.json.JSONUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class IssueIAWE3HTest {
	@Test
	void toMapTest() {
		final String jsonStr = "{\n" +
			"  \"reply\": \"hi\",\n" +
			"  \"solved\": true,\n" +
			"  \"notifyTypes\": [\n" +
			"    \"push\"\n" +
			"  ]\n" +
			"}";

		final JSONObject jsonObject = JSONUtil.parseObj(jsonStr);

		final Map<String, Object> map = jsonObject.toBean(new TypeReference<Map<String, Object>>() {});
		Assertions.assertEquals("hi", map.get("reply"));

		final Map<String, Object> map2 = jsonObject.toMap(String.class, Object.class);
		Assertions.assertEquals("hi", map2.get("reply"));
	}
}
