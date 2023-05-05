/*
 * Copyright (c) 2023 looly(loolly@aliyun.com)
 * Hutool is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 *          http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
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
