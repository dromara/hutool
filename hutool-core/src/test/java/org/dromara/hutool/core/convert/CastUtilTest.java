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
