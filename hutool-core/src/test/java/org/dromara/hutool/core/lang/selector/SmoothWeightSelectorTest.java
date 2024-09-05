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

package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SmoothWeightSelectorTest {
	@Test
	public void selectTest() {
		final SmoothWeightSelector<String> selector = SmoothWeightSelector.of();
		selector.add("A", 10);
		selector.add("B", 50);
		selector.add("C", 100);

		final String result = selector.select();
		Assertions.assertTrue(ListUtil.of("A", "B", "C").contains(result));
	}

	@Test
	public void selectCountTest() {
		final SmoothWeightSelector<String> selector = SmoothWeightSelector.of();
		selector.add("A", 10);
		selector.add("B", 50);
		selector.add("C", 100);

		final List<String> resultList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			resultList.add(selector.select());
		}

		final Map<String, Integer> countMap = CollUtil.countMap(resultList);
		Assertions.assertEquals(63, countMap.get("A"));
		Assertions.assertEquals(312, countMap.get("B"));
		Assertions.assertEquals(625, countMap.get("C"));
	}
}
