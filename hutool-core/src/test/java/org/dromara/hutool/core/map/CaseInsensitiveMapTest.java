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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaseInsensitiveMapTest {

	@Test
	public void caseInsensitiveMapTest() {
		final CaseInsensitiveMap<String, String> map = new CaseInsensitiveMap<>();
		map.put("aAA", "OK");
		assertEquals("OK", map.get("aaa"));
		assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void caseInsensitiveLinkedMapTest() {
		final CaseInsensitiveLinkedMap<String, String> map = new CaseInsensitiveLinkedMap<>();
		map.put("aAA", "OK");
		assertEquals("OK", map.get("aaa"));
		assertEquals("OK", map.get("AAA"));
	}

	@Test
	public void mergeTest(){
		//https://github.com/dromara/hutool/issues/2086
		final Map.Entry<String, String> b = MapUtil.entry("a", "value");
		final Map.Entry<String, String> a = MapUtil.entry("A", "value");
		final CaseInsensitiveMap<Object, Object> map = new CaseInsensitiveMap<>();
		map.merge(b.getKey(), b.getValue(), (A, B) -> A);
		map.merge(a.getKey(), a.getValue(), (A, B) -> A);

		assertEquals(1, map.size());
	}

	@Test
	public void issueIA4K4FTest() {
		final Map<String, Object> map = new CaseInsensitiveLinkedMap<>();
		map.put("b", 2);
		map.put("a", 1);

		final AtomicInteger index = new AtomicInteger();
		map.forEach((k, v) -> {
			if(0 == index.get()){
				assertEquals("b", k);
			} else if(1 == index.get()){
				assertEquals("a", k);
			}

			index.getAndIncrement();
		});
	}

	@Test
	public void issueIA4K4FTest2() {
		final Map<String, Object> map = new CaseInsensitiveTreeMap<>();
		map.put("b", 2);
		map.put("a", 1);

		final AtomicInteger index = new AtomicInteger();
		map.forEach((k, v) -> {
			if(0 == index.get()){
				assertEquals("a", k);
			} else if(1 == index.get()){
				assertEquals("b", k);
			}

			index.getAndIncrement();
		});
	}
}
