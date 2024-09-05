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

import org.dromara.hutool.core.lang.Console;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * 排列单元测试
 * @author looly
 *
 */
public class ArrangementTest {

	@Test
	public void arrangementTest() {
		long result = Arrangement.count(4, 2);
		Assertions.assertEquals(12, result);

		result = Arrangement.count(4, 1);
		Assertions.assertEquals(4, result);

		result = Arrangement.count(4, 0);
		Assertions.assertEquals(1, result);

		final long resultAll = Arrangement.countAll(4);
		Assertions.assertEquals(64, resultAll);
	}

	@Test
	public void selectTest() {
		final Arrangement arrangement = new Arrangement(new String[] { "1", "2", "3", "4" });
		final List<String[]> list = arrangement.select(2);
		Assertions.assertEquals(Arrangement.count(4, 2), list.size());
		Assertions.assertArrayEquals(new String[] {"1", "2"}, list.get(0));
		Assertions.assertArrayEquals(new String[] {"1", "3"}, list.get(1));
		Assertions.assertArrayEquals(new String[] {"1", "4"}, list.get(2));
		Assertions.assertArrayEquals(new String[] {"2", "1"}, list.get(3));
		Assertions.assertArrayEquals(new String[] {"2", "3"}, list.get(4));
		Assertions.assertArrayEquals(new String[] {"2", "4"}, list.get(5));
		Assertions.assertArrayEquals(new String[] {"3", "1"}, list.get(6));
		Assertions.assertArrayEquals(new String[] {"3", "2"}, list.get(7));
		Assertions.assertArrayEquals(new String[] {"3", "4"}, list.get(8));
		Assertions.assertArrayEquals(new String[] {"4", "1"}, list.get(9));
		Assertions.assertArrayEquals(new String[] {"4", "2"}, list.get(10));
		Assertions.assertArrayEquals(new String[] {"4", "3"}, list.get(11));

		final List<String[]> selectAll = arrangement.selectAll();
		Assertions.assertEquals(Arrangement.countAll(4), selectAll.size());

		final List<String[]> list2 = arrangement.select(0);
		Assertions.assertEquals(1, list2.size());
	}

	@Test
	@Disabled
	public void selectTest2() {
		final List<String[]> list = MathUtil.arrangementSelect(new String[] { "1", "1", "3", "4" });
		for (final String[] strings : list) {
			Console.log(strings);
		}
	}
}
