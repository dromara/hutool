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
import org.dromara.hutool.core.util.ObjUtil;
import org.dromara.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class TolerantMapTest {

	@Test
	public void testSerialize() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		final byte[] bytes = SerializeUtil.serialize(map);
		final TolerantMap<String, String> serializedMap = SerializeUtil.deserialize(bytes);
		assert serializedMap != map;
		assert map.equals(serializedMap);
	}

	@Test
	public void testClone() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		final TolerantMap<String, String> clonedMap = ObjUtil.clone(map);
		assert clonedMap != map;
		assert map.equals(clonedMap);
	}

	@Test
	public void testGet() {
		TolerantMap<String, String> map = TolerantMap.of(new HashMap<>(), "default");
		map.put("monday", "星期一");
		map.put("tuesday", "星期二");

		assert "星期二".equals(map.get("tuesday"));
		assert "default".equals(map.get(RandomUtil.randomStringLower(6)));
	}
}
