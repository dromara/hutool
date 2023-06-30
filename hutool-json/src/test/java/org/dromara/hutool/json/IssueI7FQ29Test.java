/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 */

package org.dromara.hutool.json;

import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

/**
 * https://gitee.com/dromara/hutool/issues/I7FQ29
 */
public class IssueI7FQ29Test {

	@Test
	void toMapTest() {
		final String jsonStr = "{\"trans_no\": \"java.lang.String\"}";
		final Map<String, Class<?>> map = JSONUtil.toBean(jsonStr, new TypeReference<Map<String, Class<?>>>() {
		});

		Assertions.assertNotNull(map);
		Assertions.assertEquals(String.class, map.get("trans_no"));
	}
}
