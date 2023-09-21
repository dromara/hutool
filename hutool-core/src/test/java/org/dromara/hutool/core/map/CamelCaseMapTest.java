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
