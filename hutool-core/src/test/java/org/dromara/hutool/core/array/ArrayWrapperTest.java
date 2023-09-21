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

package org.dromara.hutool.core.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayWrapperTest {

	@Test
	void getSubTest() {
		ArrayWrapper<int[], Object> array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5});
		int[] sub = array.getSub(1, 4);
		Assertions.assertArrayEquals(new int[]{2, 3, 4}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(1, 4);
		Assertions.assertArrayEquals(new int[]{2, 3, 4}, sub);
	}

	@Test
	void getSubStepTest() {
		ArrayWrapper<int[], Object> array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5});
		int[] sub = array.getSub(1, 4, 2);
		Assertions.assertArrayEquals(new int[]{2, 4}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(1, 4, 2);
		Assertions.assertArrayEquals(new int[]{2, 4}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(0, 5, 2);
		Assertions.assertArrayEquals(new int[]{1, 3, 5}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(0, 5, 3);
		Assertions.assertArrayEquals(new int[]{1, 4}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(1, 6, 2);
		Assertions.assertArrayEquals(new int[]{2, 4, 6}, sub);
	}
}
