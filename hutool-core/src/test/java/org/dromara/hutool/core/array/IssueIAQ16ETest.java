package org.dromara.hutool.core.array;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIAQ16ETest {

	@Test
	void lastIndexOfSubTest() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3};
		final Integer[] subBytes = new Integer[]{2, 2};
		int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		assertEquals(6, i);

		i = ArrayUtil.lastIndexOfSub(bigBytes, 3, subBytes);
		assertEquals(2, i);
	}

	@Test
	void lastIndexOfSubTest2() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3, 4, 5};
		final Integer[] subBytes = new Integer[]{2, 2, 2, 3};
		final int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		assertEquals(5, i);
	}

	@Test
	void indexOfSubTest() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3};
		final Integer[] subBytes = new Integer[]{2, 2};
		int i = ArrayUtil.indexOfSub(bigBytes, subBytes);
		assertEquals(1, i);

		i = ArrayUtil.indexOfSub(bigBytes, 3, subBytes);
		assertEquals(5, i);
	}

	@Test
	void indexOfSubTest2() {
		final Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3, 4, 5};
		final Integer[] subBytes = new Integer[]{2, 2, 2, 3};
		final int i = ArrayUtil.indexOfSub(bigBytes, subBytes);
		assertEquals(1, i);
	}
}
