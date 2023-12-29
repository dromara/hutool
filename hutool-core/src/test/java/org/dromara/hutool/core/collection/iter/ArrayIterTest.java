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

package org.dromara.hutool.core.collection.iter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * test for {@link ArrayIter}
 */
public class ArrayIterTest {

	@Test
	public void testHasNext() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertTrue(iter.hasNext());
	}

	@Test
	public void testNext() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		Assertions.assertEquals((Integer)2, iter.next());
		Assertions.assertEquals((Integer)3, iter.next());
	}

	@Test
	public void testRemove() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertThrows(UnsupportedOperationException.class, iter::remove);
	}

	@Test
	public void testGetArray() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals(arr, iter.getArray());
	}

	@Test
	public void testReset() {
		final Integer[] arr = new Integer[]{ 1, 2, 3 };
		final ArrayIter<Integer> iter = new ArrayIter<>(arr);
		Assertions.assertEquals((Integer)1, iter.next());
		iter.reset();
		Assertions.assertEquals((Integer)1, iter.next());
	}

}
