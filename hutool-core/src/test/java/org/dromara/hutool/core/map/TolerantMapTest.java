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
