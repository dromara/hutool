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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class MapBuilderTest {

	@Test
	public void conditionPutTest() {
		final Map<String, String> map = MapBuilder.<String, String>of()
				.put(true, "a", "1")
				.put(false, "b", "2")
				.put(true, "c", () -> getValue(3))
				.put(false, "d", () -> getValue(4))
				.build();

		Assertions.assertEquals(map.get("a"), "1");
		Assertions.assertFalse(map.containsKey("b"));
		Assertions.assertEquals(map.get("c"), "3");
		Assertions.assertFalse(map.containsKey("d"));
	}

	public String getValue(final int value) {
		return String.valueOf(value);
	}
}
