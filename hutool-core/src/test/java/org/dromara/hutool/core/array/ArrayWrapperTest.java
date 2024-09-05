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
