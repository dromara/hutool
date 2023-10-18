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

package org.dromara.hutool.core.map;

import org.dromara.hutool.core.text.StrUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class Issue3340Test {
	@Test
	void toCamelCaseMapTest() {
		final Map<String, Object> map = new HashMap<>();
		map.put("ID","1");
		map.put("NAME","2");
		map.put("STU_ID","3");
		final Map<String, Object> map1 = MapUtil.toCamelCaseMap(map);

		Assertions.assertEquals("2", map1.get("name"));
	}

	@Test
	void toCamelCaseTest() {
		final String str = "ID";
		Assertions.assertEquals("id", StrUtil.toCamelCase(str));
	}
}
