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

public class BiMapTest {

	@Test
	public void getTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		Assertions.assertEquals(new Integer(111), biMap.get("aaa"));
		Assertions.assertEquals(new Integer(222), biMap.get("bbb"));

		Assertions.assertEquals("aaa", biMap.getKey(111));
		Assertions.assertEquals("bbb", biMap.getKey(222));
	}

	@Test
	public void computeIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.computeIfAbsent("ccc", s -> 333);
		Assertions.assertEquals(new Integer(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}

	@Test
	public void putIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.putIfAbsent("ccc", 333);
		Assertions.assertEquals(new Integer(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}
}
