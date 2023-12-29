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

import org.dromara.hutool.core.lang.tuple.Pair;
import org.dromara.hutool.core.lang.tuple.Triple;
import org.dromara.hutool.core.lang.tuple.Tuple;
import org.dromara.hutool.core.reflect.TypeReference;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class IssueI7GPGXTest {
	@Test
	public void pairToBeanTest() {
		final Pair<String, Boolean> hutoolPair = Pair.of("test1", true);
		final String a = JSONUtil.toJsonStr(hutoolPair);
		final Pair<String, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Pair<String, Boolean>>() {});
		Assertions.assertEquals(hutoolPair, pair);
	}

	@Test
	void tripleToBeanTest() {
		final Triple<String, Integer, Boolean> hutoolTriple = Triple.of("aaa", 123, true);
		final String a = JSONUtil.toJsonStr(hutoolTriple);
		final Triple<String, Integer, Boolean> pair = JSONUtil.toBean(a, new TypeReference<Triple<String, Integer, Boolean>>() {});
		Assertions.assertEquals(hutoolTriple, pair);
	}

	@Test
	void tupleToBeanTest() {
		final Tuple hutoolTriple = Tuple.of("aaa", 123, true);
		final String a = JSONUtil.toJsonStr(hutoolTriple);
		final Tuple pair = JSONUtil.toBean(a, Tuple.class);
		Assertions.assertEquals(hutoolTriple, pair);
	}
}
