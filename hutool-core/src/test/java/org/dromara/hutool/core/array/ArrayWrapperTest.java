package org.dromara.hutool.core.array;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ArrayWrapperTest {

	@Test
	void getSubTest() {
		ArrayWrapper<int[]> array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5});
		int[] sub = array.getSub(1, 4);
		Assertions.assertArrayEquals(new int[]{2, 3, 4}, sub);

		array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5, 6});
		sub = array.getSub(1, 4);
		Assertions.assertArrayEquals(new int[]{2, 3, 4}, sub);
	}

	@Test
	void getSubStepTest() {
		ArrayWrapper<int[]> array = ArrayWrapper.of(new int[]{1, 2, 3, 4, 5});
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
