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

import java.util.HashMap;

public class SerFunctionMapTest {

	@Test
	public void putGetTest(){
		final FuncMap<Object, Object> map = new FuncMap<>(HashMap::new,
				(key)->key.toString().toLowerCase(),
				(value)->value.toString().toUpperCase());

		map.put("aaa", "b");
		map.put("BBB", "c");

		Assertions.assertEquals("B", map.get("aaa"));
		Assertions.assertEquals("C", map.get("bbb"));
	}
}
