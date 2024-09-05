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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

public class BiMapTest {

	@Test
	public void getTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		Assertions.assertEquals(Integer.valueOf(111), biMap.get("aaa"));
		Assertions.assertEquals(Integer.valueOf(222), biMap.get("bbb"));

		Assertions.assertEquals("aaa", biMap.getKey(111));
		Assertions.assertEquals("bbb", biMap.getKey(222));
	}

	@Test
	public void computeIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.computeIfAbsent("ccc", s -> 333);
		Assertions.assertEquals(Integer.valueOf(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}

	@Test
	public void putIfAbsentTest(){
		final BiMap<String, Integer> biMap = new BiMap<>(new HashMap<>());
		biMap.put("aaa", 111);
		biMap.put("bbb", 222);

		biMap.putIfAbsent("ccc", 333);
		Assertions.assertEquals(Integer.valueOf(333), biMap.get("ccc"));
		Assertions.assertEquals("ccc", biMap.getKey(333));
	}
}
