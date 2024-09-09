package cn.hutool.core.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class IssueIAQ16ETest {
	@Test
	void lastIndexOfSubTest() {
		Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3};
		Integer[] subBytes = new Integer[]{2, 2};
		final int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		Assertions.assertEquals(6, i);
	}

	@Test
	void lastIndexOfSubTest2() {
		Integer[] bigBytes = new Integer[]{1, 2, 2, 2, 3, 2, 2, 2, 3, 4, 5};
		Integer[] subBytes = new Integer[]{2, 2, 2, 3};
		final int i = ArrayUtil.lastIndexOfSub(bigBytes, subBytes);
		Assertions.assertEquals(5, i);
		Assertions.assertEquals(5, i);
	}

	@Test
	public void lastIndexOfSubTest3() {
		Integer[] a = {0x12, 0x34, 0x56, 0x78, 0x9A};
		Integer[] b = {0x56, 0x78};

		int i = ArrayUtil.lastIndexOfSub(a, b);
		assertEquals(2, i);
	}
}
