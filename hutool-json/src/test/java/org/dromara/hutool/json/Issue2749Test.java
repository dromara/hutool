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

package org.dromara.hutool.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * https://github.com/dromara/hutool/issues/2749
 * <p>
 * 由于使用了递归方式解析和写出，导致JSON太长的话容易栈溢出。
 */
public class Issue2749Test {

	@SuppressWarnings("unchecked")
	@Test
	@Disabled
	public void jsonObjectTest() {
		final Map<String, Object> map = new HashMap<>(1, 1f);
		Map<String, Object> node = map;
		for (int i = 0; i < 1000; i++) {
			node = (Map<String, Object>) node.computeIfAbsent("a", k -> new HashMap<String, Object>(1, 1f));
		}
		node.put("a", 1);
		final String jsonStr = JSONUtil.toJsonStr(map);

		@SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
		final JSONObject jsonObject = new JSONObject(jsonStr);
		Assertions.assertNotNull(jsonObject);

		// 栈溢出
		//noinspection ResultOfMethodCallIgnored
		jsonObject.toString();
	}
}
