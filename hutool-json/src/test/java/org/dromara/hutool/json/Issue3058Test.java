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
import org.junit.jupiter.api.Test;

import java.util.SortedMap;
import java.util.TreeMap;

public class Issue3058Test {
	@Test
	void toJsonStrTest() {
		final SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>() {
			private static final long serialVersionUID = 1L;
			{
				put("attributes", "a");
				put("b", "b");
				put("c", "c");
			}};

		final String jsonStr = JSONUtil.toJsonStr(sortedMap);
		Assertions.assertEquals("{\"attributes\":\"a\",\"b\":\"b\",\"c\":\"c\"}", jsonStr);
	}
}
