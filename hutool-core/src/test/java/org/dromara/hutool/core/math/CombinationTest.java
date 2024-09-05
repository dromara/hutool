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

package org.dromara.hutool.core.math;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 组合单元测试
 *
 * @author looly
 *
 */
public class CombinationTest {

	@Test
	public void countTest() {
		long result = Combination.count(5, 2);
		Assertions.assertEquals(10, result);

		result = Combination.count(5, 5);
		Assertions.assertEquals(1, result);

		result = Combination.count(5, 0);
		Assertions.assertEquals(1, result);

		final long resultAll = Combination.countAll(5);
		Assertions.assertEquals(31, resultAll);
	}

	@Test
	public void selectTest() {
		final Combination combination = new Combination(new String[] { "1", "2", "3", "4", "5" });
		final List<String[]> list = combination.select(2);
		Assertions.assertEquals(Combination.count(5, 2), list.size());

		Assertions.assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		Assertions.assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		Assertions.assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		Assertions.assertArrayEquals(new String[] {"1", "5"}, list.get(3));
		Assertions.assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		Assertions.assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		Assertions.assertArrayEquals(new String[] {"2", "5"}, list.get(6));
		Assertions.assertArrayEquals(new String[] {"3", "4"}, list.get(7));
		Assertions.assertArrayEquals(new String[] {"3", "5"}, list.get(8));
		Assertions.assertArrayEquals(new String[] {"4", "5"}, list.get(9));

		final List<String[]> selectAll = combination.selectAll();
		Assertions.assertEquals(Combination.countAll(5), selectAll.size());

		final List<String[]> list2 = combination.select(0);
		Assertions.assertEquals(1, list2.size());
	}
}
