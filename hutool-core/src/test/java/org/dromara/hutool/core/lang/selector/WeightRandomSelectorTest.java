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

package org.dromara.hutool.core.lang.selector;

import org.dromara.hutool.core.collection.CollUtil;
import org.dromara.hutool.core.collection.ListUtil;
import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class WeightRandomSelectorTest {

	@Test
	public void selectTest() {
		final WeightRandomSelector<String> random = WeightRandomSelector.of();
		random.add("A", 10);
		random.add("B", 50);
		random.add("C", 100);

		final String result = random.select();
		Assertions.assertTrue(ListUtil.of("A", "B", "C").contains(result));
	}

	@Test
	@Disabled
	public void selectCountTest() {
		final WeightRandomSelector<String> random = WeightRandomSelector.of();
		random.add("A", 10);
		random.add("B", 50);
		random.add("C", 100);

		final List<String> resultList = new ArrayList<>();
		for (int i = 0; i < 1000; i++) {
			resultList.add(random.select());
		}

		Console.log(CollUtil.countMap(resultList));
	}
}
