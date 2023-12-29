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

package org.dromara.hutool.core.convert;

import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.collection.set.SetUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CastUtilTest {
	@Test
	public void testCastToSuper() {
		final Collection<Integer> collection= ListUtil.of(1,2,3);
		final List<Integer> list = ListUtil.of(1, 2, 3);
		final Set<Integer> set = SetUtil.of(1, 2, 3);
		final Map<Integer, Integer> map = new HashMap<>();
		map.put(1, 1);

		final Collection<Number> collection2 = CastUtil.castUp(collection);
		collection2.add(new Double("123.1"));
		Assertions.assertSame(collection, collection2);

		final Collection<Integer> collection3 = CastUtil.castDown(collection2);
		Assertions.assertSame(collection2, collection3);

		final List<Number> list2 = CastUtil.castUp(list);
		Assertions.assertSame(list, list2);
		final List<Integer> list3 = CastUtil.castDown(list2);
		Assertions.assertSame(list2, list3);

		final Set<Number> set2 = CastUtil.castUp(set);
		Assertions.assertSame(set, set2);
		final Set<Integer> set3 = CastUtil.castDown(set2);
		Assertions.assertSame(set2, set3);

		final Map<Number, Serializable> map2 = CastUtil.castUp(map);
		Assertions.assertSame(map, map2);
		final Map<Integer, Number> map3 = CastUtil.castDown(map2);
		Assertions.assertSame(map2, map3);
	}
}
