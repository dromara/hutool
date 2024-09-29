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
import org.dromara.hutool.json.JSONUtil;
import org.dromara.hutool.json.serializer.JSONDeserializer;
import org.dromara.hutool.json.serializer.TypeAdapterManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * https://gitee.com/dromara/hutool/issues/I7FQ29
 */
public class IssueI7FQ29Test {

	@Test
	void toMapTest() {
		// Class不添加默认反序列化器，防止可能的安全问题
		TypeAdapterManager.getInstance().register(Class.class, (JSONDeserializer<Class<?>>) (json, deserializeType) -> {
			try {
				return Class.forName(json.asJSONPrimitive().getValue().toString());
			} catch (final ClassNotFoundException e) {
				throw new RuntimeException(e);
			}
		});

		final String jsonStr = "{\"trans_no\": \"java.lang.String\"}";
		final Map<String, Class<?>> map = JSONUtil.toBean(jsonStr, new TypeReference<Map<String, Class<?>>>() {
		});

		Assertions.assertNotNull(map);
		Assertions.assertEquals(String.class, map.get("trans_no"));
	}
}
