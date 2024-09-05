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
