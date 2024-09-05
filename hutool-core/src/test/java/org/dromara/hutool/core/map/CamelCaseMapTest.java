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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.io.SerializeUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CamelCaseMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		final CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("customKey", "OK");
		Assertions.assertEquals("OK", map.get("customKey"));
		Assertions.assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		final CamelCaseLinkedMap<String, String> map = new CamelCaseLinkedMap<>();
		map.put("customKey", "OK");
		Assertions.assertEquals("OK", map.get("customKey"));
		Assertions.assertEquals("OK", map.get("custom_key"));
	}

	@Test
	public void serializableKeyFuncTest() {
		final CamelCaseMap<String, String> map = new CamelCaseMap<>();
		map.put("serializable_key", "OK");
		final CamelCaseMap<String, String> deSerializableMap = SerializeUtil.deserialize(SerializeUtil.serialize(map));
		Assertions.assertEquals("OK", deSerializableMap.get("serializable_key"));
		Assertions.assertEquals("OK", deSerializableMap.get("serializableKey"));
		deSerializableMap.put("serializable_func", "OK");
		Assertions.assertEquals("OK", deSerializableMap.get("serializable_func"));
		Assertions.assertEquals("OK", deSerializableMap.get("serializableFunc"));
	}
}
