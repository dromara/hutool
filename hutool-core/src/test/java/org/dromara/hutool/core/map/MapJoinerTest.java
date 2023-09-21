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

public class MapJoinerTest {

	@Test
	public void joinMapTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, null);

		Assertions.assertEquals("id-12+name-张三+age-23", joiner.toString());
	}

	@Test
	public void joinMapWithPredicateTest(){
		final Dict v1 = Dict.of().set("id", 12).set("name", "张三").set("age", 23);
		final MapJoiner joiner = MapJoiner.of("+", "-");
		joiner.append(v1, (entry)->"age".equals(entry.getKey()));

		Assertions.assertEquals("age-23", joiner.toString());
	}
}
